package pl.miloszgilga.archiver.scrapper.scrap;

import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.archiver.scrapper.db.DataHandler;
import pl.miloszgilga.archiver.scrapper.gui.MessageDialog;
import pl.miloszgilga.archiver.scrapper.gui.window.AbstractWindow;
import pl.miloszgilga.archiver.scrapper.gui.window.RootWindow;
import pl.miloszgilga.archiver.scrapper.soup.*;
import pl.miloszgilga.archiver.scrapper.state.AppState;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.List;

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
    dayScheduleSource = new TvChannelDayScheduleSource(rootState.getBrowserManager(),
      selectedChannel.slug());
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
      endDate = LocalDate
        .of(selectedYear, Month.DECEMBER, 1)
        .with(TemporalAdjusters.lastDayOfMonth());
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
    log.info("Staring scrap data from channel: {} with max delay: {}s",
      selectedChannel, maxDelayMs / 1000);

    long totalProcessesDays = totalFetchedCount;
    for (final LocalDate date : datesRange) {
      if (!isScrappingActive) {
        break;
      }
      final long randomnessWaitingTime = minDelayMs + random.nextLong(maxDelayMs - minDelayMs + 1);
      final List<DayScheduleDetails> details = dayScheduleSource.fetchDayScheduleDetails(date);
      dataHandler.batchInsertChannelData(selectedChannel, details, date);
      try {
        sleep(randomnessWaitingTime);
      } catch (InterruptedException ex) {
        log.error("Scrapping thread interrupted. Cause: {}", ex.getMessage());
        break;
      }
      rootState.updateTotalFetchedCount(++totalProcessesDays);
      rootState.updateChannelDetails(updateYearFetchedProgramsCount(date));
      log.info("Add {} programs data: {} with randomness: {}ms", details.size(), date,
        randomnessWaitingTime);
    }
    log.info("Stopped scrap data from: {} with processed days count: {}", selectedChannel,
      totalProcessesDays);
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

  private List<LocalDate> generateDateRange(
    LocalDate startDate,
    LocalDate endDate,
    List<LocalDate> skippedDates
  ) {
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
