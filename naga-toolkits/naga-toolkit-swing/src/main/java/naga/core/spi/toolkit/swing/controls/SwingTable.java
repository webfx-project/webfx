package naga.core.spi.toolkit.swing.controls;

import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.propertymarkers.SelectionMode;
import naga.core.spi.toolkit.swing.node.SwingSelectableDisplayResultSetNode;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayselection.DisplaySelection;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTable extends SwingSelectableDisplayResultSetNode<JScrollPane> implements Table<JScrollPane> {

    private final JTable table;
    private final DisplayTableModel tableModel = new DisplayTableModel();

    public SwingTable() {
        this(createTransparentScrollPane(createTable()));
    }

    public SwingTable(JScrollPane tableScrollPane) {
        super(tableScrollPane);
        table = (JTable) tableScrollPane.getViewport().getView();
        table.setModel(tableModel);
        syncVisualSelectionMode(getSelectionMode());
        displaySelectionProperty().addListener((observable, oldValue, newValue) -> syncVisualDisplaySelection());
        table.getSelectionModel().addListSelectionListener(e -> syncToolkitDisplaySelection());
    }

    private static JTable createTable() {
        JTable table = new JTable();
        table.setGridColor(new Color(221, 221, 221));
        return table;
    }

    private static JScrollPane createTransparentScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setOpaque(false);
        scrollPane.setFont(null);
        scrollPane.setBorder(null);
        JViewport viewport = scrollPane.getViewport();
        viewport.setOpaque(false);
        viewport.setFont(null);
        viewport.setBorder(null);
        return scrollPane;
    }

    private boolean syncingDisplaySelection;

    private void syncToolkitDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            setDisplaySelection(DisplaySelection.createRowsSelection(table.getSelectedRows()));
            syncingDisplaySelection = false;
        }
    }

    private void syncVisualDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            DisplaySelection displaySelection = getDisplaySelection();
            ListSelectionModel selectionModel = table.getSelectionModel();
            selectionModel.clearSelection();
            if (displaySelection != null)
                displaySelection.forEachRow(row -> selectionModel.addSelectionInterval(row, row));
            syncingDisplaySelection = false;
        }
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
        int swingSelectionMode = 0;
        switch (selectionMode) {
            case DISABLED:
            case SINGLE:
                swingSelectionMode = ListSelectionModel.SINGLE_SELECTION;
                break;
            case MULTIPLE:
                swingSelectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
                break;
        }
        table.getSelectionModel().setSelectionMode(swingSelectionMode);
    }


    @Override
    protected void syncVisualDisplayResult(DisplayResultSet displayResultSet) {
        tableModel.setDisplayResultSet(displayResultSet);
        tableModel.fireTableStructureChanged();
        for (int columnIndex = 0; columnIndex < displayResultSet.getColumnCount(); columnIndex++)
            table.getColumnModel().getColumn(columnIndex).setHeaderValue(displayResultSet.getColumns()[columnIndex].getName());
        //tableModel.fireTableDataChanged();
        table.doLayout();
    }

    private static class DisplayTableModel extends AbstractTableModel {

        private DisplayResultSet displayResultSet;

        void setDisplayResultSet(DisplayResultSet displayResultSet) {
            this.displayResultSet = displayResultSet;
        }

        @Override
        public int getRowCount() {
            return displayResultSet == null ? 0 : displayResultSet.getRowCount();
        }

        @Override
        public int getColumnCount() {
            return displayResultSet == null ? 0 : displayResultSet.getColumnCount();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return displayResultSet == null ? null : displayResultSet.getValue(rowIndex, columnIndex);
        }
    }
}
