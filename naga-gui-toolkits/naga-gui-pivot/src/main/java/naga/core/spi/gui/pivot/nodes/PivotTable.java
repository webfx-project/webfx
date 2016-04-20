package naga.core.spi.gui.pivot.nodes;

import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.nodes.Table;
import naga.core.util.Strings;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.HashMap;
import org.apache.pivot.wtk.*;


/**
 * @author Bruno Salmon
 */
public class PivotTable extends PivotDisplayNode<ScrollPane> implements Table<ScrollPane> {

    private TableView tableView;

    public PivotTable() {
        this(createScrollPane());
    }

    public PivotTable(ScrollPane scrollPane) {
        super(scrollPane);
        tableView = (TableView) node.getView();
    }

    private static ScrollPane createScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        TableView tableView = new TableView();
        scrollPane.setView(tableView);
        scrollPane.setColumnHeader(new TableViewHeader(tableView));
        return scrollPane;
    }

    @Override
    protected void onNextDisplayResult(DisplayResult displayResult) {
        int rowCount = displayResult.getRowCount();
        int columnCount = displayResult.getColumnCount();
        TableView.ColumnSequence columns = tableView.getColumns();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            if (columnIndex < columns.getLength())
                setUpTableColumn(columns.get(columnIndex), columnIndex, displayResult);
            else
                columns.add(setUpTableColumn(new TableView.Column(Strings.toString(displayResult.getHeaderValues()[columnIndex])), columnIndex, displayResult));
        }
        ArrayList tableData = new ArrayList(rowCount);
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            HashMap row = new HashMap();
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                row.put(Integer.toString(columnIndex), displayResult.getValue(rowIndex, columnIndex));
            tableData.add(row);
        }
        tableView.setTableData(tableData);
    }

    private TableView.Column setUpTableColumn(TableView.Column tableColumn, int columnIndex, DisplayResult displayResult) {
        tableColumn.setName(Integer.toString(columnIndex));
        tableColumn.setHeaderData(Strings.toString(displayResult.getHeaderValues()[columnIndex]));
        tableColumn.setWidth(400);
        return tableColumn;
    }

}
