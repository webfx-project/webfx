package naga.core.spi.gui.gwtmaterial.nodes;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.nodes.Table;
import naga.core.util.Strings;
import naga.core.util.collection.IdentityList;

/**
 * @author Bruno Salmon
 */
public class GwtTable extends GwtDisplayNode<DataGrid<Integer>> implements Table<DataGrid<Integer>> {

    public GwtTable() {
        this(new DataGrid<>());
    }

    public GwtTable(DataGrid<Integer> node) {
        super(node);
        node.addStyleName("bordered");
    }

    @Override
    protected void onNextDisplayResult(DisplayResult displayResult) {
        for (int columnIndex = 0; columnIndex < displayResult.getColumnCount(); columnIndex++) {
            GwtColumn column;
            if (columnIndex < node.getColumnCount())
                column = (GwtColumn) node.getColumn(columnIndex);
            else
                node.addColumn(column = new GwtColumn(columnIndex), Strings.toString(displayResult.getHeaderValues()[columnIndex]));
            column.displayResult = displayResult;
        }
        int rowCount = displayResult.getRowCount();
        if (node.getRowCount() != rowCount) {
            node.setRowCount(0, true);
            node.setRowData(new IdentityList(rowCount));
            node.setRowCount(rowCount, true);
            node.redraw(); // otherwise the change on setRowData() is not considered
        }
    }

    private static class GwtColumn extends TextColumn<Integer> {
        private final int columnIndex;
        private DisplayResult displayResult;

        public GwtColumn(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public String getValue(Integer rowIndex) {
            return (String) displayResult.getValue(rowIndex, columnIndex);
        }
    }
}
