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

package pl.miloszgilga.tvarchiver.webscrapper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.miloszgilga.tvarchiver.webscrapper.db.YearWithPersistedDto;
import pl.miloszgilga.tvarchiver.webscrapper.gui.FrameTaskbar;
import pl.miloszgilga.tvarchiver.webscrapper.gui.panel.ChannelDetailsPanel;
import pl.miloszgilga.tvarchiver.webscrapper.scrap.DataScrapperThread;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannel;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannelCalendarSource;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannelDetails;
import pl.miloszgilga.tvarchiver.webscrapper.soup.TvChannelYearData;
import pl.miloszgilga.tvarchiver.webscrapper.state.AppState;
import pl.miloszgilga.tvarchiver.webscrapper.state.RootState;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ChannelDetailsController {
	private final ChannelDetailsPanel channelDetailsPanel;
	private DataScrapperThread dataScrapperThread;

	public void removeSelectedYear() {
		final RootState rootState = channelDetailsPanel.getRootState();
		rootState.updateSelectedYear(StringUtils.EMPTY);
		log.info("Expand search content for: {}", rootState.getSelectedChannel().name());
	}

	public void startScrapping() {
		final RootState rootState = channelDetailsPanel.getRootState();
		dataScrapperThread = new DataScrapperThread(rootState, 0);
		dataScrapperThread.start();
		rootState.updateAppState(AppState.SCRAPPING);
		updateProgressState(Taskbar.State.NORMAL);
	}

	public void stopScrapping() {
		final RootState rootState = channelDetailsPanel.getRootState();
		dataScrapperThread.stopScrapping();
		rootState.updateAppState(AppState.IDLE);
		updateProgressState(Taskbar.State.PAUSED);
	}

	public void onUpdateRandomness() {
		channelDetailsPanel.getRootState().updateRandomness(channelDetailsPanel.getRandomnessValueSlider().getValue());
	}

	public void onSwitchChannel(TvChannel channel) {
		if (channel.name().isBlank()) {
			return;
		}
		final RootState rootState = channelDetailsPanel.getRootState();
		final JdbcTemplate jdbcTemplate = rootState.getJdbcTemplate();

		// check count of saved programs and compare with all dates
		Long persistedTvPrograms = jdbcTemplate
			.queryForObject("SELECT COUNT(*) FROM tv_programs_data WHERE channel_id = ?", Long.class, channel.id());
		if (persistedTvPrograms == null) {
			persistedTvPrograms = 0L;
		}

		// fetch already persisted count rows per year
		final String sql = """
			SELECT YEAR(schedule_date) as year, COUNT(*) AS count FROM tv_programs_data
			WHERE channel_id = ? GROUP BY YEAR(schedule_date)
			""";
		final List<YearWithPersistedDto> alreadyPersistedPerYear = jdbcTemplate
			.query(sql, (rs, rowNum) -> new YearWithPersistedDto(rs.getInt("year"), rs.getInt("count")), channel.id());

		// scrap tv channel details (count of records, start and end date)
		final TvChannelCalendarSource tvChannelCalendarSource = new TvChannelCalendarSource(channel.slug());
		final TvChannelDetails details = tvChannelCalendarSource.getSelectedTvChannelDetails();

		final Map<Integer, Long> persistedYears = alreadyPersistedPerYear.stream()
			.collect(Collectors.toMap(YearWithPersistedDto::year, YearWithPersistedDto::count));

		// assign existing data to persisted years
		for (final Map.Entry<Integer, TvChannelYearData> year : details.years().entrySet()) {
			if (persistedYears.containsKey(year.getKey())) {
				year.getValue().setFetchedCount(persistedYears.get(year.getKey()));
			}
		}
		rootState.updateSelectedYear(-1);
		rootState.updateTotalFetchedCount(persistedTvPrograms);
		rootState.updateChannelDetails(details);
	}

	private void updateProgressState(Taskbar.State state) {
		FrameTaskbar.setProgressState(channelDetailsPanel.getRootWindow(), state);
	}
}
