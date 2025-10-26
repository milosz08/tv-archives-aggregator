package pl.miloszgilga.archiver.scrapper.gui.panel;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import pl.miloszgilga.archiver.scrapper.controller.ChannelDetailsController;
import pl.miloszgilga.archiver.scrapper.gui.window.RootWindow;
import pl.miloszgilga.archiver.scrapper.state.ChannelDetailsTotalFetchedAggregator;
import pl.miloszgilga.archiver.scrapper.state.RootState;
import pl.miloszgilga.archiver.scrapper.util.Constant;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Hashtable;

@Getter
public class ChannelDetailsPanel extends JPanel {
  private static final int SIZE = 60;
  private static final int STEP = 5;

  private final RootState rootState;
  private final RootWindow rootWindow;
  private final ChannelDetailsController controller;
  private final JPanel controlPanel;
  private final ScrappingDetailsPanel scrappingDetailsPanel;

  private final JSlider randomnessValueSlider;
  private final Border sliderFrame, sliderMargin;
  private final JButton removeSelectedYearButton;
  private final JButton removeRowDataButton;
  private final JButton startScrappingButton;
  private final JButton stopScrappingButton;
  private final JPanel progressBarPanel;
  private final JProgressBar progressBar;

  public ChannelDetailsPanel(RootState rootState, RootWindow rootWindow) {
    this.rootState = rootState;
    this.rootWindow = rootWindow;
    controller = new ChannelDetailsController(this, rootWindow.getMessageDialog());

    controlPanel = new JPanel();
    scrappingDetailsPanel = new ScrappingDetailsPanel(rootState);

    randomnessValueSlider = new JSlider(JSlider.VERTICAL, 5, SIZE, rootState.getRandomness());
    sliderFrame = BorderFactory.createTitledBorder("Randomness");
    sliderMargin = BorderFactory.createEmptyBorder(0, 20, 0, 20);
    removeSelectedYearButton = new JButton("Remove selection");
    removeRowDataButton = new JButton("Remove row data");
    startScrappingButton = new JButton("Start scrapping");
    stopScrappingButton = new JButton("Stop scrapping");
    progressBarPanel = new JPanel();
    progressBar = new JProgressBar();

    randomnessValueSlider.setBorder(BorderFactory.createCompoundBorder(sliderFrame, sliderMargin));
    randomnessValueSlider.setMinorTickSpacing(STEP);
    randomnessValueSlider.setMajorTickSpacing(STEP);
    randomnessValueSlider.setPaintTicks(true);
    randomnessValueSlider.setPaintLabels(true);
    randomnessValueSlider.setLabelTable(createSliderLabels());
    randomnessValueSlider.addChangeListener(e -> controller.onUpdateRandomness());

    stopScrappingButton.setEnabled(false);

    progressBar.setMinimum(0);
    progressBar.setMaximum(100);
    progressBar.setStringPainted(true);
    progressBar.setString(progressBar.getValue() + "%");

    removeSelectedYearButton.addActionListener(e -> controller.removeSelectedYear());
    removeRowDataButton.addActionListener(e -> controller.removeRowData());
    startScrappingButton.addActionListener(e -> controller.startScrapping());
    stopScrappingButton.addActionListener(e -> controller.stopScrapping());

    progressBarPanel.setLayout(new GridLayout(1, 1));
    progressBarPanel.add(progressBar);

    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
    controlPanel.add(removeSelectedYearButton);
    controlPanel.add(removeRowDataButton);
    controlPanel.add(progressBarPanel);
    controlPanel.add(startScrappingButton);
    controlPanel.add(stopScrappingButton);

    setLayout(new BorderLayout());

    add(scrappingDetailsPanel, BorderLayout.CENTER);
    add(randomnessValueSlider, BorderLayout.EAST);
    add(controlPanel, BorderLayout.SOUTH);

    initObservables();
  }

  private Hashtable<Integer, JLabel> createSliderLabels() {
    final Hashtable<Integer, JLabel> labelsTable = new Hashtable<>();
    for (int i = 0; i <= SIZE; i += STEP) {
      if (i != 0) {
        labelsTable.put(i, new JLabel(i + "s"));
      }
    }
    return labelsTable;
  }

  private void updateProgress(double percentage, long fetched, long total) {
    SwingUtilities.invokeLater(() -> {
      progressBar.setValue((int) percentage);
      progressBar.setString(String.format("%s%% (%d/%d)", Constant.PF.format(percentage),
        fetched, total));
      rootWindow.updateTitle(percentage);
    });
  }

  private void initObservables() {
    final Observable<ChannelDetailsTotalFetchedAggregator> aggregator = Observable.combineLatest(
      rootState.getChannelDetails$(),
      rootState.getTotalFetchedCount$(),
      ChannelDetailsTotalFetchedAggregator::new
    );
    rootState.asDisposable(rootState.getSelectedChannel$(), controller::onSwitchChannel);
    rootState.asDisposable(rootState.getSelectedYear$(), year -> {
      startScrappingButton
        .setText("Start scrapping" + (year != -1 ? " (" + year + ")" : ""));
      removeSelectedYearButton.setEnabled(year != -1);
      removeRowDataButton.setEnabled(year != -1);
    });
    rootState.asDisposable(aggregator, state -> {
      if (state.details() != null) {
        final long total = state.details().daysCount();
        final double percentage = ((double) state.totalFetched() / total) * 100;
        updateProgress(percentage, state.totalFetched(), total);
        rootWindow.getFrameTaskbar().setProgress(percentage);
      }
    });
    rootState.asDisposable(rootState.getAppState$(), state -> {
      randomnessValueSlider.setEnabled(state.isIdle());
      startScrappingButton.setEnabled(state.isIdle());
      stopScrappingButton.setEnabled(!state.isIdle());
    });
  }
}
