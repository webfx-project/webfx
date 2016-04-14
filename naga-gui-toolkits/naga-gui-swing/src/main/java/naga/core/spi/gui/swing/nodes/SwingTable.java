package naga.core.spi.gui.swing.nodes;

import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.nodes.Table;
import naga.core.util.Strings;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTable extends SwingDisplayNode<JScrollPane> implements Table<JScrollPane> {

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
    protected void onNextDisplayResult(DisplayResult displayResult) {
        tableModel.setDisplayResult(displayResult);
        tableModel.fireTableStructureChanged();
        for (int columnIndex = 0; columnIndex < displayResult.getColumnCount(); columnIndex++)
            table.getColumnModel().getColumn(columnIndex).setHeaderValue(Strings.toString(displayResult.getHeaderValues()[columnIndex]));
        //tableModel.fireTableDataChanged();
        table.doLayout();
    }

    /**
     * @author Bruno Salmon
     */
    public static class DisplayTableModel extends AbstractTableModel {

        private DisplayResult displayResult;

        public void setDisplayResult(DisplayResult displayResult) {
            this.displayResult = displayResult;
        }

        @Override
        public int getRowCount() {
            return displayResult == null ? 0 : displayResult.getRowCount();
        }

        @Override
        public int getColumnCount() {
            return displayResult == null ? 0 :displayResult.getColumnCount();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return displayResult == null ? null : displayResult.getValue(rowIndex, columnIndex);
        }
    }
}
