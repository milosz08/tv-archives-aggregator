package pl.miloszgilga.archiver.scrapper.gui.panel;

import lombok.Getter;
import pl.miloszgilga.archiver.scrapper.controller.ConsolePanelController;
import pl.miloszgilga.archiver.scrapper.gui.AppIcon;
import pl.miloszgilga.archiver.scrapper.gui.component.JAppIconButton;
import pl.miloszgilga.archiver.scrapper.gui.window.AbstractWindow;

import javax.swing.*;
import java.awt.*;

@Getter
public class ConsolePanel extends JPanel {
  public static JTextArea textArea;

  private final ConsolePanelController controller;
  private final JScrollPane scrollPane;
  private final JToolBar rightButtonsToolbar;

  private final JAppIconButton moveToUpButton;
  private final JAppIconButton moveToDownButton;
  private final JAppIconButton clearButton;
  private final JAppIconButton printToFileButton;

  public ConsolePanel(AbstractWindow rootWindow) {
    controller = new ConsolePanelController(this, rootWindow.getMessageDialog());

    setLayout(new BorderLayout());

    textArea = new JTextArea();
    scrollPane = new JScrollPane(textArea);
    rightButtonsToolbar = new JToolBar();

    textArea.setEditable(false);
    textArea.setFont(new Font("monospaced", textArea.getFont().getStyle(), textArea.getFont().getSize()));

    rightButtonsToolbar.setLayout(new BoxLayout(rightButtonsToolbar, BoxLayout.Y_AXIS));
    rightButtonsToolbar.setFloatable(false);

    moveToUpButton = new JAppIconButton("Move to up", AppIcon.UPLOAD, true);
    moveToDownButton = new JAppIconButton("Move to down", AppIcon.DOWNLOAD, true);
    clearButton = new JAppIconButton("Clear", AppIcon.DELETE, true);
    printToFileButton = new JAppIconButton("Print to file", AppIcon.PRINT, true);

    moveToUpButton.addActionListener(e -> controller.moveUp());
    moveToDownButton.addActionListener(e -> controller.moveDown());
    clearButton.addActionListener(e -> controller.clearText());
    printToFileButton.addActionListener(e -> controller.printToFile());

    rightButtonsToolbar.add(moveToUpButton);
    rightButtonsToolbar.add(moveToDownButton);
    rightButtonsToolbar.add(clearButton);
    rightButtonsToolbar.add(printToFileButton);

    add(scrollPane, BorderLayout.CENTER);
    add(rightButtonsToolbar, BorderLayout.EAST);
  }
}
