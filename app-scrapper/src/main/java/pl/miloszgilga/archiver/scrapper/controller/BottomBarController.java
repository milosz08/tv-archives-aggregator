package pl.miloszgilga.archiver.scrapper.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import pl.miloszgilga.archiver.scrapper.db.DataHandler;
import pl.miloszgilga.archiver.scrapper.gui.panel.BottomBarPanel;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class BottomBarController {
  private final BottomBarPanel bottomBarPanel;
  @Getter
  private final Timer jvmMeasurementsTimer, dbSizeMeasurementsTimer;

  public BottomBarController(BottomBarPanel bottomBarPanel) {
    this.bottomBarPanel = bottomBarPanel;
    jvmMeasurementsTimer = new Timer(5000, e -> updateMemoryUsage());
    dbSizeMeasurementsTimer = new Timer(10000, e -> fetchDatabaseSize());
  }

  public String getMemoryUsage() {
    return parseBytes("Memory", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
  }

  public String parseToDbSize(long bytes) {
    return parseBytes("DB size", bytes);
  }

  public String parseToDbHost() {
    final InetSocketAddress address = bottomBarPanel.getRootState().getDataHandler().getDbHost();
    return String.format("DB: %s:%s", address.getHostString(), address.getPort());
  }

  public void updateMemoryUsage() {
    bottomBarPanel.getMemUsageLabel().setText(getMemoryUsage());
  }

  public void fetchDatabaseSize() {
    final RootState rootState = bottomBarPanel.getRootState();
    // fetch only when is running and after took first size
    final DataHandler dataHandler = rootState.getDataHandler();
    final Long dbSize = dataHandler.getDatabaseSize();
    if (dbSize != null) {
      bottomBarPanel.getDbSizeLabel().setText(parseToDbSize(dbSize));
    }
  }

  private String parseBytes(String prefix, long bytes) {
    String formattedBytes;
    try {
      formattedBytes = FileUtils.byteCountToDisplaySize(bytes);
    } catch (IllegalArgumentException ex) {
      formattedBytes = "0MB";
    }
    return String.format("%s: %s", prefix, formattedBytes);
  }
}
