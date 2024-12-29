/*
 * Copyright (c) 2024 by Miłosz Gilga <https://miloszgilga.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.miloszgilga.tvarchiver.webscrapper.scrap;

import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.tvarchiver.webscrapper.db.DataHandler;
import pl.miloszgilga.tvarchiver.webscrapper.gui.MessageDialog;
import pl.miloszgilga.tvarchiver.webscrapper.gui.window.AbstractWindow;
import pl.miloszgilga.tvarchiver.webscrapper.gui.window.RootWindow;
import pl.miloszgilga.tvarchiver.webscrapper.soup.*;
import pl.miloszgilga.tvarchiver.webscrapper.state.AppState;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.*;

@Slf4j
public class DataScrapperThread extends Thread {
	private final RootState rootState;
	private final AbstractWindow rootWindow;
	private final MessageDialog messageDialog;
	private final TvChannel selectedChannel;
	private final long minDelayMs, maxDelayMs;
	private final Random random;
	private final TvChannelDayScheduleSource dayScheduleSource;
	private final int selectedYear;
	private final TvChannelDetails tvChannelDetails;
	private final long totalFetchedCount;

	private boolean isScrappingActive;

	public DataScrapperThread(RootWindow rootWindow, int minDelay) {
		super("ScrapperThread");
		this.rootWindow = rootWindow;
		this.rootState = rootWindow.getRootState();
		this.messageDialog = rootWindow.getMessageDialog();
		this.selectedChannel = rootState.getSelectedChannel();
		minDelayMs = minDelay * 1000L;
		maxDelayMs = rootState.getRandomness() * 1000L;
		random = new Random();
		dayScheduleSource = new TvChannelDayScheduleSource(selectedChannel.slug());
		selectedYear = rootState.getSelectedYear();
		tvChannelDetails = rootState.getTvChannelDetails();
		totalFetchedCount = rootState.getTotalFetchedCount();
	}

	@Override
	public void run() {
		final DataHandler dataHandler = rootState.getDataHandler();

		final Map<Integer, TvChannelYearData> years = tvChannelDetails.years();
		final int firstYear = Collections.min(years.keySet());
		final int lastYear = Collections.max(years.keySet());

		// only for selected year, for non-selected year, pick start and end date
		LocalDate startDate;
		LocalDate endDate;
		if (selectedYear != -1) {
			startDate = LocalDate.of(selectedYear, Month.JANUARY, 1);
			endDate = LocalDate.of(selectedYear, Month.DECEMBER, 1).with(TemporalAdjusters.lastDayOfMonth());
			// if its first year, pick start day
			if (selectedYear == firstYear) {
				startDate = tvChannelDetails.startDate();
			} else if (selectedYear == lastYear) { // if its last year, pick end day
				endDate = tvChannelDetails.endDate();
			}
		} else {
			startDate = tvChannelDetails.startDate();
			endDate = tvChannelDetails.endDate();
		}
		// check already saved dates
		final List<LocalDate> alreadySavedDates = dataHandler
			.getAlreadySavedDates(selectedChannel.slug(), startDate, endDate);

		// dates range
		final List<LocalDate> datesRange = generateDateRange(startDate, endDate, alreadySavedDates);
		final long totalDaysToProcess = datesRange.size();

		log.info("Pre-scrap info: start: {}, end: {}, days: {}. Skipping: {} days", startDate, endDate,
			totalDaysToProcess, alreadySavedDates.size());

		isScrappingActive = true;
		log.info("Staring scrap data from channel: {} with max delay: {}s", selectedChannel, maxDelayMs / 1000);

		long totalProcessesDays = totalFetchedCount;
		for (final LocalDate date : datesRange) {
			if (!isScrappingActive) {
				break;
			}
			final long randomnessWaitingTime = minDelayMs + random.nextLong(maxDelayMs - minDelayMs + 1);
			final List<DayScheduleDetails> details = dayScheduleSource.fetchDayScheduleDetails(date);
			dataHandler.batchInsertChannelData(selectedChannel.slug(), details, date);
			try {
				sleep(randomnessWaitingTime);
			} catch (InterruptedException ex) {
				log.error("Scrapping thread interrupted. Cause: {}", ex.getMessage());
				break;
			}
			rootState.updateTotalFetchedCount(++totalProcessesDays);
			rootState.updateChannelDetails(updateYearFetchedProgramsCount(date));
			log.info("Add {} programs data: {} with randomness: {}s", details.size(), date, randomnessWaitingTime);
		}
		log.info("Stopped scrap data from: {} with processed records count: {}", selectedChannel, totalProcessesDays);
		rootState.updateAppState(AppState.IDLE);

		boolean isEnded;
		long processedDays;
		if (selectedYear != -1) {
			// check, if fetching data for selected year is ended
			final TvChannelYearData year = rootState.getTvChannelDetails().years().get(selectedYear);
			isEnded = year.getFetchedCount() == year.getTotalCount();
			processedDays = year.getTotalCount();
		} else {
			// check for all data from all years
			isEnded = totalProcessesDays == tvChannelDetails.daysCount();
			processedDays = totalProcessesDays;
		}
		if (isEnded) {
			messageDialog.showInfo("Scrapping ended. Processed %d days.", processedDays);
			rootState.updateSelectedYear(-1);
		}
		rootWindow.getFrameTaskbar().setProgressState(Taskbar.State.PAUSED);
	}

	public void stopScrapping() {
		isScrappingActive = false;
	}

	private List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate, List<LocalDate> skippedDates) {
		final List<LocalDate> dates = new ArrayList<>();
		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {
			dates.add(currentDate);
			currentDate = currentDate.plusDays(1);
		}
		dates.removeAll(skippedDates);
		return dates;
	}

	private TvChannelDetails updateYearFetchedProgramsCount(LocalDate processingDate) {
		final TvChannelDetails details = rootState.getTvChannelDetails();
		final TvChannelYearData year = details.years().get(processingDate.getYear());
		year.setFetchedCount(year.getFetchedCount() + 1);
		return details;
	}
}
