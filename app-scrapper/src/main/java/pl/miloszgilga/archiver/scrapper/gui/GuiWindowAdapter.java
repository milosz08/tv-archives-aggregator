package pl.miloszgilga.archiver.scrapper.gui;

import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Slf4j
public class GuiWindowAdapter extends WindowAdapter {
  private final MessageDialog messageDialog;
  private final RootState rootState;

  private boolean isScrapping;

  public GuiWindowAdapter(MessageDialog messageDialog, RootState rootState) {
    this.messageDialog = messageDialog;
    this.rootState = rootState;
    initObservables();
  }

  @Override
  public void windowClosing(WindowEvent e) {
    if (isScrapping) {
      messageDialog.showError("You cannot close the window while scrapping is active!");
      return;
    }
    final int result = messageDialog.showConfirm("Are you sure you want to exit?");
    if (result == JOptionPane.YES_OPTION) {
      rootState.cleanupAndDisposableSubscription();
      System.exit(0);
    }
  }

  private void initObservables() {
    rootState.asDisposable(rootState.getAppState$(), state -> isScrapping = !state.isIdle());
  }
}
