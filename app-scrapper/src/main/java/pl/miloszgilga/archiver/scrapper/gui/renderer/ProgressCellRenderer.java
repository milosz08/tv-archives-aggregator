package pl.miloszgilga.archiver.scrapper.gui.renderer;

import pl.miloszgilga.archiver.scrapper.util.Constant;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ProgressCellRenderer extends DefaultTableCellRenderer {
  private final JProgressBar progressBar;

  public ProgressCellRenderer() {
    progressBar = new JProgressBar(0, 100);
    progressBar.setStringPainted(true);
  }

  @Override
  public Component getTableCellRendererComponent(
    JTable table,
    Object value,
    boolean isSelected,
    boolean hasFocus,
    int row,
    int column
  ) {
    final double percentageLevel = (Double) value;
    progressBar.setValue((int) percentageLevel);
    progressBar.setString(Constant.PF.format(percentageLevel) + "%");
    return progressBar;
  }
}
