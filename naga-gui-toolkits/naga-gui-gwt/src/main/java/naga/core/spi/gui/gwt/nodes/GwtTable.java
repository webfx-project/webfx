package naga.core.spi.gui.gwt.nodes;

import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.gui.nodes.Table;
import naga.core.util.Strings;
import naga.core.util.collection.IdentityList;

/**
 * @author Bruno Salmon
 */
public class GwtTable extends GwtDisplayResultSetNode<AbstractCellTable<Integer>> implements Table<AbstractCellTable<Integer>> {

    public GwtTable() {
        this(new CellTable<>());
    }

    public GwtTable(AbstractCellTable<Integer> node) {
        super(node);
    }

    @Override
    protected void onNextDisplayResult(DisplayResultSet displayResultSet) {
        //Platform.log("Updating GwtTable: " + displayResultSet.getRowCount() + " rows & " + displayResultSet.getColumnCount() + " columns");
        for (int columnIndex = 0; columnIndex < displayResultSet.getColumnCount(); columnIndex++) {
            GwtColumn column;
            if (columnIndex < node.getColumnCount())
                column = (GwtColumn) node.getColumn(columnIndex);
            else
                node.addColumn(column = new GwtColumn(columnIndex), Strings.stringValue(displayResultSet.getHeaderValues()[columnIndex]));
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

        public GwtColumn(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public String getValue(Integer rowIndex) {
            return (String) displayResultSet.getValue(rowIndex, columnIndex);
        }
    }
}
