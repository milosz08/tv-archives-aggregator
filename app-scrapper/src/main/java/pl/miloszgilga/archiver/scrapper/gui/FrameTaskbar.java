package pl.miloszgilga.archiver.scrapper.gui;

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
public class FrameTaskbar {
  private static final Taskbar taskbar = Taskbar.getTaskbar();
  private final JFrame rootFrame;

  public void setProgressState(Taskbar.State state) {
    if (taskbar != null) {
      taskbar.setWindowProgressState(rootFrame, state);
    }
  }

  public void setProgress(Double percentage) {
    if (taskbar != null) {
      taskbar.setWindowProgressValue(rootFrame, percentage.intValue());
    }
  }
}
