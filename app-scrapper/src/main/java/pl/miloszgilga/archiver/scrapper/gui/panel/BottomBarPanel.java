package pl.miloszgilga.archiver.scrapper.gui.panel;

import lombok.Getter;
import pl.miloszgilga.archiver.scrapper.controller.BottomBarController;
import pl.miloszgilga.archiver.scrapper.state.AppState;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import java.awt.*;

@Getter
public class BottomBarPanel extends JPanel {
  private final RootState rootState;
  private final BottomBarController controller;

  private final JLabel stateLabel;
  private final JLabel dbHostLabel;
  private final JLabel dbSizeLabel;
  private final JLabel memUsageLabel;

  public BottomBarPanel(RootState rootState) {
    this.rootState = rootState;
    controller = new BottomBarController(this);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    stateLabel = new JLabel(AppState.IDLE.createState());
    dbHostLabel = new JLabel(controller.parseToDbHost());
    dbSizeLabel = new JLabel(controller.parseToDbSize(0L));
    memUsageLabel = new JLabel(controller.getMemoryUsage());

    add(Box.createHorizontalGlue());
    addComponentWithBreadcrumb(stateLabel);
    addComponentWithBreadcrumb(dbHostLabel);
    addComponentWithBreadcrumb(dbSizeLabel);
    addComponentWithBreadcrumb(memUsageLabel);

    controller.updateMemoryUsage();
    controller.fetchDatabaseSize();
    controller.getJvmMeasurementsTimer().start();

    initObservables();
  }

  private void addComponentWithBreadcrumb(JLabel label) {
    label.setAlignmentX(Component.RIGHT_ALIGNMENT);
    add(label);
    add(Box.createHorizontalStrut(20));
  }

  private void initObservables() {
    rootState.asDisposable(rootState.getAppState$(), appState -> {
      stateLabel.setText(appState.createState());
      final Timer dbSizeMeasurementsTimer = controller.getDbSizeMeasurementsTimer();
      if (appState.isIdle()) {
        dbSizeMeasurementsTimer.start();
      } else {
        dbSizeMeasurementsTimer.stop();
      }
    });
  }
}
