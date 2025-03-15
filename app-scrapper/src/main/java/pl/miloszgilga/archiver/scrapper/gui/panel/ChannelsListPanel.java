package pl.miloszgilga.archiver.scrapper.gui.panel;

import lombok.Getter;
import pl.miloszgilga.archiver.scrapper.controller.ChannelsListController;
import pl.miloszgilga.archiver.scrapper.gui.filter.TvChannelFilterListener;
import pl.miloszgilga.archiver.scrapper.gui.window.AbstractWindow;
import pl.miloszgilga.archiver.scrapper.soup.TvChannel;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import java.awt.*;

@Getter
public class ChannelsListPanel extends JPanel {
  private final RootState rootState;
  private final ChannelsListController controller;

  private final JPanel channelsPanel;

  private final JTextField searchField;
  private final DefaultListModel<TvChannel> channelListModel;
  private final JList<TvChannel> channels;
  private final JScrollPane scrollPane;

  private final JPanel buttonWithTextPanel;
  private final JButton reFetchChannelsButton;
  private final JButton removeSelectionButton;
  private final JLabel fetchedChannelsLabel;

  public ChannelsListPanel(RootState rootState, AbstractWindow rootWindow) {
    this.rootState = rootState;
    controller = new ChannelsListController(this, rootWindow.getMessageDialog());

    channelsPanel = new JPanel();
    searchField = new JTextField();

    channelListModel = new DefaultListModel<>();
    channels = new JList<>(channelListModel);
    channels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    channels.addListSelectionListener(controller::onListSelection);
    scrollPane = new JScrollPane(channels);

    buttonWithTextPanel = new JPanel();
    reFetchChannelsButton = new JButton("Re-fetch TV channels");
    removeSelectionButton = new JButton("Remove selection");
    fetchedChannelsLabel = new JLabel("Found 0 TV channels");

    reFetchChannelsButton.addActionListener(e -> controller.reFetchChannels());
    removeSelectionButton.addActionListener(e -> controller.removeSelection());

    buttonWithTextPanel.setLayout(new GridLayout(3, 1));
    buttonWithTextPanel.add(reFetchChannelsButton);
    buttonWithTextPanel.add(removeSelectionButton);
    buttonWithTextPanel.add(fetchedChannelsLabel);

    setLayout(new BorderLayout());
    add(searchField, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(buttonWithTextPanel, BorderLayout.SOUTH);

    initObservables();

    controller.fetchChannelsList();
    searchField.getDocument().addDocumentListener(new TvChannelFilterListener(controller));
  }

  private void initObservables() {
    rootState.asDisposable(rootState.getTvChannels$(), channelListModel::addAll);
    rootState.asDisposable(rootState.getAppState$(), state -> {
      reFetchChannelsButton.setEnabled(state.isIdle());
      removeSelectionButton.setEnabled(state.isIdle());
      channels.setEnabled(state.isIdle());
    });
  }
}
