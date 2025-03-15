package pl.miloszgilga.archiver.scrapper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.miloszgilga.archiver.scrapper.gui.panel.ScrappingDetailsPanel;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

@Slf4j
@RequiredArgsConstructor
public class ScrappingDetailsController {
  private final ScrappingDetailsPanel scrappingDetailsPanel;

  public void onRowSelection(ListSelectionEvent event) {
    if (event.getValueIsAdjusting()) {
      return;
    }
    final RootState rootState = scrappingDetailsPanel.getRootState();
    final JTable detailsTable = scrappingDetailsPanel.getDetailsTable();
    final int selectedRowIndex = detailsTable.getSelectedRow();
    if (selectedRowIndex == -1) {
      return;
    }
    final int selectedYear = (Integer) detailsTable.getValueAt(selectedRowIndex, 0);
    rootState.updateSelectedYear(selectedYear);
    log.info("Narrow search content for: {} to year: {}", rootState.getSelectedChannel().name(),
      selectedYear);
  }
}
