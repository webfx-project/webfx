package naga.core.spi.toolkit.swing.nodes;

import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.nodes.Table;
import naga.core.util.Strings;

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
    }

    @Override
    protected void onNextSelectionMode(SelectionMode selectionMode) {

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


    @Override
    protected void onNextDisplayResult(DisplayResultSet displayResultSet) {
        tableModel.setDisplayResultSet(displayResultSet);
        tableModel.fireTableStructureChanged();
        for (int columnIndex = 0; columnIndex < displayResultSet.getColumnCount(); columnIndex++)
            table.getColumnModel().getColumn(columnIndex).setHeaderValue(Strings.toString(displayResultSet.getColumns()[columnIndex].getHeaderValue()));
        //tableModel.fireTableDataChanged();
        table.doLayout();
    }

    /**
     * @author Bruno Salmon
     */
    public static class DisplayTableModel extends AbstractTableModel {

        private DisplayResultSet displayResultSet;

        public void setDisplayResultSet(DisplayResultSet displayResultSet) {
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
