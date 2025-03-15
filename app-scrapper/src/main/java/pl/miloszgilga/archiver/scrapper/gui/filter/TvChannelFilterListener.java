package pl.miloszgilga.archiver.scrapper.gui.filter;

import lombok.RequiredArgsConstructor;
import pl.miloszgilga.archiver.scrapper.controller.ChannelsListController;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@RequiredArgsConstructor
public class TvChannelFilterListener implements DocumentListener {
  private final ChannelsListController controller;

  @Override
  public void insertUpdate(DocumentEvent e) {
    controller.performListFiltering();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    controller.performListFiltering();
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    controller.performListFiltering();
  }
}
