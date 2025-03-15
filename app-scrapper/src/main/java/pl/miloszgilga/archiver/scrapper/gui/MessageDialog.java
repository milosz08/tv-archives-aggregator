package pl.miloszgilga.archiver.scrapper.gui;

import lombok.RequiredArgsConstructor;

import javax.swing.*;

@RequiredArgsConstructor
public class MessageDialog {
  private final JFrame frame;

  public void showError(String message) {
    JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public void showInfo(String message, Object... args) {
    JOptionPane.showMessageDialog(frame, String.format(message, args));
  }

  public int showConfirm(String message, Object... args) {
    return JOptionPane.showConfirmDialog(
      frame,
      String.format(message, args),
      "Confirmation",
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE
    );
  }
}
