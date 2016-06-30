package naga.core.spi.toolkit.gwt.controls;

import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.gwt.node.GwtSelectableDisplayResultSetNode;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.util.collection.IdentityList;

/**
 * @author Bruno Salmon
 */
public class GwtTable extends GwtSelectableDisplayResultSetNode<AbstractCellTable<Integer>> implements Table<AbstractCellTable<Integer>> {

    public GwtTable() {
        this(new DataGrid<>()); // CellTable for height automatically set to the number of rows, DataGrid for fixed height with scroll bar
    }

    public GwtTable(AbstractCellTable<Integer> node) {
        super(node);
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet displayResultSet) {
        //Platform.log("Updating GwtTable: " + displayResultSet.getRowCount() + " rows & " + displayResultSet.getColumnCount() + " columns");
        for (int columnIndex = 0; columnIndex < displayResultSet.getColumnCount(); columnIndex++) {
            GwtColumn column;
            if (columnIndex < node.getColumnCount())
                column = (GwtColumn) node.getColumn(columnIndex);
            else
                node.addColumn(column = new GwtColumn(columnIndex), displayResultSet.getColumns()[columnIndex].getName());
            column.displayResultSet = displayResultSet;
        }
        int rowCount = displayResultSet.getRowCount();
        if (node.getRowCount() != rowCount) {
            node.setRowCount(0, true);
            node.setRowData(new IdentityList(rowCount));
            node.setRowCount(rowCount, true);
            node.redraw(); // otherwise the change on setRowData() is not considered
        }
        //Platform.log("GwtTable updated");
    }

    private static class GwtColumn extends TextColumn<Integer> {
        private final int columnIndex;
        private DisplayResultSet displayResultSet;

        GwtColumn(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public String getValue(Integer rowIndex) {
            // Returning a null value causes an uncaught JavaScript exception (hard to debug)
            Object value = displayResultSet.getValue(rowIndex, columnIndex);
            return value == null ? "" : value.toString();
        }
    }
}
