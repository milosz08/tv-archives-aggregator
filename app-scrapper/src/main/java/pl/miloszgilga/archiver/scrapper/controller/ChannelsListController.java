package pl.miloszgilga.archiver.scrapper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.archiver.scrapper.db.DataHandler;
import pl.miloszgilga.archiver.scrapper.gui.MessageDialog;
import pl.miloszgilga.archiver.scrapper.gui.panel.ChannelsListPanel;
import pl.miloszgilga.archiver.scrapper.soup.TvChannel;
import pl.miloszgilga.archiver.scrapper.soup.TvChannelsSource;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ChannelsListController {
  private final ChannelsListPanel channelsListPanel;
  private final MessageDialog messageDialog;

  public void fetchChannelsList() {
    final RootState rootState = channelsListPanel.getRootState();
    final DataHandler dataHandler = rootState.getDataHandler();
    List<TvChannel> channels;
    if (dataHandler == null) {
      return; // skipping, not initialized DataHandler class
    }
    channelsListPanel.getChannelListModel().clear();
    rootState.updateTvChannels(new ArrayList<>());
    rootState.updateSelectedChannel(new TvChannel());
    // firstly, select data from persisted DB
    final List<TvChannel> fetchedTvChannels = dataHandler.getTvChannels();
    if (fetchedTvChannels.isEmpty()) {
      // if is empty, scrap data and insert into db
      final TvChannelsSource tvChannelsSource = new TvChannelsSource(rootState.getBrowserManager());
      final List<TvChannel> scrappedTvChannels = tvChannelsSource.getAllTvChannels();

      dataHandler.batchInsertTvChannels(scrappedTvChannels);
      log.info("Persisted DB data empty. Filling with {} fetched channels.",
        scrappedTvChannels.size());
      channels = scrappedTvChannels;
    } else {
      log.info("Found {} persisted channels.", fetchedTvChannels.size());
      channels = fetchedTvChannels;
    }
    rootState.updateTvChannels(channels);
    updateFetchedTvChannels(channels.size());
  }

  public void reFetchChannels() {
    fetchChannelsList();
    messageDialog.showInfo("TV channels list was restored.");
  }

  public void removeSelection() {
    channelsListPanel.getChannels().clearSelection();
    channelsListPanel.getRootState().updateSelectedChannel(new TvChannel());
  }

  public void onListSelection(ListSelectionEvent event) {
    if (!event.getValueIsAdjusting()) {
      final RootState rootState = channelsListPanel.getRootState();
      final JList<TvChannel> tvChannels = channelsListPanel.getChannels();
      if (tvChannels.getSelectedIndex() != -1) {
        rootState.updateSelectedChannel(tvChannels.getSelectedValue());
      }
    }
  }

  public void performListFiltering() {
    final String searchTerm = channelsListPanel.getSearchField().getText()
      .toLowerCase()
      .trim();
    final RootState rootState = channelsListPanel.getRootState();
    final DefaultListModel<TvChannel> tvChannelsModel = channelsListPanel.getChannelListModel();

    tvChannelsModel.clear();
    for (final TvChannel tvChannel : rootState.getTvChannels()) {
      final String name = tvChannel.name().toLowerCase();
      if (searchTerm.isEmpty() || name.contains(searchTerm)) {
        tvChannelsModel.addElement(tvChannel);
      }
    }
    updateFetchedTvChannels(tvChannelsModel.size());
  }

  private void updateFetchedTvChannels(int size) {
    channelsListPanel.getFetchedChannelsLabel().setText(String.format("Found %d TV channels", size));
  }
}
