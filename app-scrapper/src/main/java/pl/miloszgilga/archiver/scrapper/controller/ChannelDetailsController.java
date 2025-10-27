package pl.miloszgilga.archiver.scrapper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.archiver.scrapper.db.DataHandler;
import pl.miloszgilga.archiver.scrapper.db.YearWithPersistedDto;
import pl.miloszgilga.archiver.scrapper.gui.MessageDialog;
import pl.miloszgilga.archiver.scrapper.gui.panel.ChannelDetailsPanel;
import pl.miloszgilga.archiver.scrapper.gui.window.AbstractWindow;
import pl.miloszgilga.archiver.scrapper.gui.window.RootWindow;
import pl.miloszgilga.archiver.scrapper.scrap.DataScrapperThread;
import pl.miloszgilga.archiver.scrapper.soup.TvChannel;
import pl.miloszgilga.archiver.scrapper.soup.TvChannelCalendarSource;
import pl.miloszgilga.archiver.scrapper.soup.TvChannelDetails;
import pl.miloszgilga.archiver.scrapper.soup.TvChannelYearData;
import pl.miloszgilga.archiver.scrapper.state.AppState;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ChannelDetailsController {
  private final ChannelDetailsPanel channelDetailsPanel;
  private final MessageDialog messageDialog;
  private DataScrapperThread dataScrapperThread;

  public void removeSelectedYear() {
    final RootState rootState = channelDetailsPanel.getRootState();
    rootState.updateSelectedYear(-1);
    log.info("Expand search content for: {}", rootState.getSelectedChannel().name());
  }

  public void removeRowData() {
    final RootState rootState = channelDetailsPanel.getRootState();
    final int selectedYear = rootState.getSelectedYear();
    final TvChannel selectedChannel = rootState.getSelectedChannel();
    if (selectedYear == -1 || selectedChannel == null) {
      return; // not selected specific year, skipping
    }
    final TvChannelYearData yearData = rootState.getTvChannelDetails().years().get(selectedYear);
    final int response = messageDialog.showConfirm(
      "Are you sure to remove %d data from year %d",
      yearData.getFetchedCount(), selectedYear
    );
    if (response == JOptionPane.YES_OPTION) {
      final DataHandler dataHandler = rootState.getDataHandler();
      final int rowsAffected = dataHandler
        .deleteChannelDataByYear(selectedChannel.slug(), selectedYear);

      messageDialog.showInfo("Successfully deleted %s rows.", rowsAffected);
      rootState.updateSelectedYear(-1);
      yearData.setFetchedCount(0);
      rootState.updateSelectedChannel(selectedChannel);
    }
  }

  public void startScrapping() {
    final RootState rootState = channelDetailsPanel.getRootState();
    final int selectedYear = rootState.getSelectedYear();
    boolean alreadyScrapper;
    if (selectedYear != -1) { // fetched all data from selected year
      final var yearDetails = rootState.getTvChannelDetails().years().get(selectedYear);
      alreadyScrapper = yearDetails.getFetchedCount() == yearDetails.getTotalCount();
    } else { // fetched all data for selected channel
      alreadyScrapper = rootState.getTvChannelDetails().daysCount() == rootState
        .getTotalFetchedCount();
    }
    if (alreadyScrapper) {
      messageDialog.showInfo("All data for year %s was already fetched.", selectedYear);
      return;
    }
    dataScrapperThread = new DataScrapperThread(channelDetailsPanel.getRootWindow(), 0);
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
    channelDetailsPanel.getRootState()
      .updateRandomness(channelDetailsPanel.getRandomnessValueSlider().getValue());
  }

  public void onSwitchChannel(TvChannel channel) {
    final RootWindow rootWindow = channelDetailsPanel.getRootWindow();
    if (channel.name().isBlank()) {
      rootWindow.setDefaultTitle();
      updateProgressState(Taskbar.State.NORMAL);
      return;
    }
    final RootState rootState = channelDetailsPanel.getRootState();
    final DataHandler dataHandler = rootState.getDataHandler();

    dataHandler.createChannelTableIfNotExists(channel.slug());

    // check count of saved programs and compare with all dates
    final Long persistedTvPrograms = dataHandler.getPersistedProgramsPerYear(channel.slug());

    // fetch already persisted count rows per year
    final List<YearWithPersistedDto> alreadyPersistedPerYear = dataHandler
      .getAlreadyPersistedPerYear(channel.slug());

    // scrap tv channel details (count of records, start and end date)
    final var tvChannelCalendarSource = new TvChannelCalendarSource(rootState.getBrowserManager(),
      channel.slug());
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
    rootWindow.updateTitle(channel.name());
    updateProgressState(Taskbar.State.PAUSED);
  }

  private void updateProgressState(Taskbar.State state) {
    final AbstractWindow rootWindow = channelDetailsPanel.getRootWindow();
    rootWindow.getFrameTaskbar().setProgressState(state);
  }
}
