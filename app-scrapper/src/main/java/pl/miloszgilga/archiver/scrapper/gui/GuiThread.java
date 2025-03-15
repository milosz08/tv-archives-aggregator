package pl.miloszgilga.archiver.scrapper.gui;

import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.archiver.scrapper.gui.window.ConnectToDbWindow;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

@Slf4j
public class GuiThread implements Runnable {
  private final LookAndFeel defaultLookAndFeel;
  private final RootState rootState;
  private final MessageDialog messageDialog;

  public GuiThread() {
    defaultLookAndFeel = new MetalLookAndFeel();
    rootState = new RootState();
    messageDialog = new MessageDialog(null);
  }

  public void initAndStartThread() {
    try {
      UIManager.setLookAndFeel(defaultLookAndFeel);
      SwingUtilities.invokeLater(this);
    } catch (UnsupportedLookAndFeelException ex) {
      log.error(ex.getMessage());
    }
  }

  @Override
  public void run() {
    log.info("Starting GUI thread");
    try {
      final ConnectToDbWindow rootWindow = new ConnectToDbWindow(rootState);
      rootWindow.createWindow();
      log.info("Initialized application GUI.");
    } catch (InoperableException ex) {
      log.error(ex.getMessage());
      messageDialog.showError(ex.getMessage());
    }
  }
}
