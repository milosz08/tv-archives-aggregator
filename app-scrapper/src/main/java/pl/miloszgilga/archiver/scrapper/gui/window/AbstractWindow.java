package pl.miloszgilga.archiver.scrapper.gui.window;

import lombok.Getter;
import pl.miloszgilga.archiver.scrapper.gui.FrameTaskbar;
import pl.miloszgilga.archiver.scrapper.gui.GuiWindowAdapter;
import pl.miloszgilga.archiver.scrapper.gui.MessageDialog;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractWindow extends JFrame {
  private final String title;
  private final Dimension size;
  private final GuiWindowAdapter guiWindowAdapter;
  private final JPanel rootPanel;
  @Getter
  private final MessageDialog messageDialog;
  @Getter
  private final FrameTaskbar frameTaskbar;

  public AbstractWindow(String title, int width, int height, RootState rootState) {
    this.title = title;
    rootPanel = new JPanel();
    size = new Dimension(width, height);
    messageDialog = new MessageDialog(this);
    guiWindowAdapter = new GuiWindowAdapter(messageDialog, rootState);
    frameTaskbar = new FrameTaskbar(this);
  }

  public void createWindow() {
    rootPanel.setBounds(0, 0, getWidth(), getHeight());
    setSize(size);
    setMaximumSize(size);
    setMinimumSize(size);
    setLocation(getMotherScreenCenter());
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setResizable(false);
    addWindowListener(guiWindowAdapter);
    setTitle(title);
    setLayout(new BorderLayout());
    add(rootPanel, BorderLayout.CENTER);
    appendElements(this, rootPanel);
    setVisible(true);
    pack();
  }

  public void closeWindow() {
    setVisible(false);
    dispose();
  }

  private Point getMotherScreenCenter() {
    final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    final int x = dimension.width / 2 - getWidth() / 2;
    final int y = dimension.height / 2 - getHeight() / 2;
    return new Point(x, y);
  }

  abstract void appendElements(JFrame frame, JPanel rootPanel);
}
