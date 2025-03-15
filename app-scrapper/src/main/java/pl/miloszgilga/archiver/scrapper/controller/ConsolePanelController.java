package pl.miloszgilga.archiver.scrapper.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import pl.miloszgilga.archiver.scrapper.gui.MessageDialog;
import pl.miloszgilga.archiver.scrapper.gui.panel.ConsolePanel;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class ConsolePanelController {
  private final ConsolePanel consolePanel;
  private final MessageDialog messageDialog;

  public void moveUp() {
    final JScrollBar verticalScrollBar = consolePanel.getScrollPane().getVerticalScrollBar();
    verticalScrollBar.setValue(verticalScrollBar.getMinimum());
  }

  public void moveDown() {
    final JScrollBar verticalScrollBar = consolePanel.getScrollPane().getVerticalScrollBar();
    verticalScrollBar.setValue(verticalScrollBar.getMaximum());
  }

  public void clearText() {
    final int result = messageDialog.showConfirm("Are you sure to clear console content?");
    if (result == JOptionPane.YES_OPTION) {
      final JTextArea textArea = ConsolePanel.textArea;
      textArea.setText(StringUtils.EMPTY);
    }
  }

  public void printToFile() {
    final FastDateFormat targetFormat = FastDateFormat.getInstance("MM-dd-yyyy-HH-mm-ss-SSS");
    final String fileName = String.format("%s-%s", targetFormat.format(new Date()), "log.txt");

    final JFileChooser fileChooser = new JFileChooser();
    fileChooser.setSelectedFile(new File(fileName));

    final int result = fileChooser.showSaveDialog(null);
    if (result != JFileChooser.APPROVE_OPTION) {
      return;
    }
    final File file = fileChooser.getSelectedFile();
    try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(ConsolePanel.textArea.getText());
      messageDialog.showInfo("Logs saved to: " + file.getAbsolutePath());
    } catch (IOException ex) {
      messageDialog.showError("An error occurred while saving logs to file!");
    }
  }
}
