package pl.miloszgilga.archiver.scrapper.gui.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ColoredYearsTableRenderer extends DefaultTableCellRenderer {
  @Override
  public Component getTableCellRendererComponent(
    JTable table,
    Object value,
    boolean isSelected,
    boolean hasFocus,
    int row,
    int column
  ) {
    final Component cell = super
      .getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    cell.setFont(cell.getFont().deriveFont(Font.BOLD, 15));

    // colored row based data acquisition
    final long remaining = (long) table.getModel().getValueAt(row, 3);
    final double percentage = (double) table.getModel().getValueAt(row, 4);

    Color color;
    if (remaining > 0) {
      color = percentage > 50 ? Color.ORANGE : Color.BLACK;
    } else {
      color = Color.GREEN;
    }
    cell.setForeground(color.darker());
    return cell;
  }
}
