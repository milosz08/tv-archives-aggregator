package pl.miloszgilga.archiver.scrapper.gui.panel;

import io.reactivex.rxjava3.core.Observable;
import lombok.Getter;
import pl.miloszgilga.archiver.scrapper.controller.ScrappingDetailsController;
import pl.miloszgilga.archiver.scrapper.gui.renderer.ColoredYearsTableRenderer;
import pl.miloszgilga.archiver.scrapper.gui.renderer.ProgressCellRenderer;
import pl.miloszgilga.archiver.scrapper.soup.TvChannelYearData;
import pl.miloszgilga.archiver.scrapper.state.ChannelDetailsTotalFetchedAggregator;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

@Getter
public class ScrappingDetailsPanel extends JPanel {
  private final RootState rootState;
  private final ScrappingDetailsController controller;

  private final JPanel channelTitlePanel;
  private final JLabel channelTitle;

  private final DefaultTableModel detailsTableModel;
  private final JTable detailsTable;
  private final JScrollPane detailsScrollPane;

  private final Object[] columnNames = {"Year", "Fetched", "All", "Remaining", "Percentage"};

  public ScrappingDetailsPanel(RootState rootState) {
    this.rootState = rootState;
    controller = new ScrappingDetailsController(this);

    setLayout(new BorderLayout());

    channelTitlePanel = new JPanel();
    channelTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

    channelTitle = new JLabel(parseChannelName("-", 0));
    channelTitle.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD, 17));

    detailsTableModel = new DefaultTableModel(new Object[][]{}, columnNames);
    detailsTable = new JTable(detailsTableModel);
    detailsScrollPane = new JScrollPane(detailsTable);

    detailsTable.setDefaultEditor(Object.class, null);
    detailsTable.setDefaultRenderer(Object.class, new ColoredYearsTableRenderer());
    detailsTable.getColumnModel().getColumn(4).setCellRenderer(new ProgressCellRenderer());
    detailsTable.getSelectionModel().addListSelectionListener(controller::onRowSelection);
    detailsTable.setRowHeight(22);
    detailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    detailsTable.setDragEnabled(false);
    detailsTable.getTableHeader().setReorderingAllowed(false);

    setColumnWidth(0, 100);
    setColumnWidth(1, 150);
    setColumnWidth(2, 150);
    setColumnWidth(3, 150);

    channelTitlePanel.add(channelTitle);

    add(channelTitlePanel, BorderLayout.NORTH);
    add(detailsScrollPane, BorderLayout.CENTER);

    initObservables();
  }

  private String parseChannelName(String name, long id) {
    return String.format("%s #%d", name, id);
  }

  private void setColumnWidth(int colIndex, int width) {
    detailsTable.getColumnModel().getColumn(colIndex).setPreferredWidth(width);
    detailsTable.getColumnModel().getColumn(colIndex).setMaxWidth(width);
  }

  private void initObservables() {
    final Observable<ChannelDetailsTotalFetchedAggregator> aggregator = Observable.combineLatest(
      rootState.getChannelDetails$(),
      rootState.getTotalFetchedCount$(),
      ChannelDetailsTotalFetchedAggregator::new
    );
    rootState.asDisposable(aggregator, state -> {
      if (state.details() != null && (state.details().daysCount() == state.totalFetched())) {
        detailsTable.clearSelection();
        rootState.updateSelectedYear(-1);
      }
    });
    rootState.asDisposable(rootState.getSelectedChannel$(), channel -> channelTitle
      .setText(parseChannelName(channel.name(), channel.id())));
    rootState.asDisposable(rootState.getSelectedYear$(), year -> {
      if (year == -1) {
        detailsTable.clearSelection();
      }
    });
    rootState.asDisposable(rootState.getAppState$(), state -> detailsTable.setEnabled(state.isIdle()));
    rootState.asDisposable(rootState.getChannelDetails$(), details -> {
      detailsTableModel.setRowCount(0);
      for (final Map.Entry<Integer, TvChannelYearData> entry : details.years().entrySet()) {
        final TvChannelYearData data = entry.getValue();
        detailsTableModel.addRow(new Object[]{
          entry.getKey(),
          data.getFetchedCount(),
          data.getTotalCount(),
          data.getTotalCount() - data.getFetchedCount(),
          ((double) data.getFetchedCount() / data.getTotalCount()) * 100
        });
      }
    });
  }
}
