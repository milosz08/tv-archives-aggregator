package pl.miloszgilga.archiver.scrapper;

import pl.miloszgilga.archiver.scrapper.gui.GuiThread;

public class AppScrapperMain {
  public static void main(String[] args) {
    final GuiThread guiThread = new GuiThread();
    guiThread.initAndStartThread();
  }
}
