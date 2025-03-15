package pl.miloszgilga.archiver.scrapper.gui.panel;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class UnselectedChannelPanel extends JPanel {
  public UnselectedChannelPanel() {
    final JLabel placeholder = new JLabel("Select TV channel");
    setLayout(new MigLayout());
    add(placeholder, "push, align center");
  }
}
