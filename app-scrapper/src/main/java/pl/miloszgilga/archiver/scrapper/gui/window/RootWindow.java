package pl.miloszgilga.archiver.scrapper.gui.window;

import lombok.Getter;
import pl.miloszgilga.archiver.scrapper.gui.panel.*;
import pl.miloszgilga.archiver.scrapper.state.RootState;
import pl.miloszgilga.archiver.scrapper.util.Constant;

import javax.swing.*;
import java.awt.*;
import java.util.StringJoiner;

@Getter
public class RootWindow extends AbstractWindow {
  public static final String DEFAULT_TITLE = "TV Scrapper";

  private final RootState rootState;

  private final ChannelsListPanel channelsListPanel;
  private final JPanel rightCombinePanel;
  private final JPanel tvChannelContainerPanel;
  private final UnselectedChannelPanel unselectedChannelPanel;
  private final ChannelDetailsPanel channelDetailsPanel;
  private final ConsolePanel consolePanel;
  private final BottomBarPanel bottomBarPanel;

  public RootWindow(RootState rootState) {
    super(DEFAULT_TITLE, 1280, 720, rootState);
    this.rootState = rootState;

    channelsListPanel = new ChannelsListPanel(rootState, this);
    rightCombinePanel = new JPanel();
    tvChannelContainerPanel = new JPanel();
    unselectedChannelPanel = new UnselectedChannelPanel();
    channelDetailsPanel = new ChannelDetailsPanel(rootState, this);
    consolePanel = new ConsolePanel(this);
    bottomBarPanel = new BottomBarPanel(rootState);
  }

  @Override
  void appendElements(JFrame frame, JPanel rootPanel) {
    channelsListPanel.setPreferredSize(new Dimension(250, 720));
    unselectedChannelPanel.setPreferredSize(new Dimension(1030, 380));
    channelDetailsPanel.setPreferredSize(new Dimension(1030, 380));
    consolePanel.setPreferredSize(new Dimension(1030, 340));

    tvChannelContainerPanel.setLayout(new CardLayout());
    tvChannelContainerPanel.add(unselectedChannelPanel, "unselected");
    tvChannelContainerPanel.add(channelDetailsPanel, "selected");

    rightCombinePanel.setLayout(new BoxLayout(rightCombinePanel, BoxLayout.Y_AXIS));
    rightCombinePanel.add(tvChannelContainerPanel);
    rightCombinePanel.add(consolePanel);

    final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, channelsListPanel,
      rightCombinePanel);
    splitPane.setResizeWeight(0.25);
    splitPane.setDividerLocation(250);

    rootPanel.setLayout(new BorderLayout());
    rootPanel.add(splitPane, BorderLayout.CENTER);
    rootPanel.add(bottomBarPanel, BorderLayout.SOUTH);

    initObservables();
  }

  private void initObservables() {
    rootState.asDisposable(rootState.getSelectedChannel$(), channel -> {
      final CardLayout cardLayout = (CardLayout) tvChannelContainerPanel.getLayout();
      cardLayout.show(tvChannelContainerPanel, channel.id() == 0 ? "unselected" : "selected");
    });
  }

  public void updateTitle(String channelName) {
    setTitle(DEFAULT_TITLE + " - " + channelName);
  }

  public void updateTitle(Double percentage) {
    final String[] fragments = getTitle().split(" - ");
    final StringJoiner joiner = new StringJoiner(" - ");
    joiner.add(DEFAULT_TITLE);
    joiner.add(fragments[1]);
    joiner.add(Constant.PF.format(percentage) + "%");
    setTitle(joiner.toString());
  }

  public void setDefaultTitle() {
    setTitle(DEFAULT_TITLE);
  }
}
