package naga.core.spi.toolkit.gwtpolymer.nodes;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.vaadin.Cell;
import com.vaadin.polymer.vaadin.Column;
import com.vaadin.polymer.vaadin.Row;
import com.vaadin.polymer.vaadin.widget.VaadinGrid;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.toolkit.gwt.nodes.GwtDisplayResultSetNode;
import naga.core.spi.toolkit.nodes.Table;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerTable extends GwtDisplayResultSetNode<VaadinGrid> implements Table<VaadinGrid> {

    public GwtPolymerTable() {
        this(new VaadinGrid());
    }

    public GwtPolymerTable(VaadinGrid node) {
        super(node);
    }

    @Override
    protected void onNextDisplayResult(DisplayResultSet displayResultSet) {
        Polymer.ready(node.getElement(), o -> {
            //Platform.log("Updating GwtPolymerTable: " + displayResultSet.getRowCount() + " rows & " + displayResultSet.getColumnCount() + " columns");
            //node.getSelection().setMode("multi");
            JsArray columns = node.getColumns();
            for (int columnIndex = 0; columnIndex < displayResultSet.getColumnCount(); columnIndex++) {
                Column column;
                if (columnIndex < columns.length())
                    column = columns.get(columnIndex).cast();
                else
                    node.addColumn(column = JavaScriptObject.createObject().cast(), null);
                // The API says to use column.setContentHeader() but it doesn't work so using column.setName() instead
                column.setName(Strings.stringValue(displayResultSet.getHeaderValues()[columnIndex]));
                final int colIndex = columnIndex;
                column.setRenderer(oCell -> {
                    Cell cell = (Cell) oCell;
                    Row row = cell.getRow().cast();
                    int rowIndex = (int) row.getIndex();
                    Object value = displayResultSet.getValue(rowIndex, colIndex);
                    String text = value == null ? "" : value.toString();
                    cell.getElement().<Element>cast().setInnerHTML("<span style='overflow: hidden; text-overflow: ellipsis;'>" + text + "</span>");
                    return null;
                });
            }
            int rowCount = displayResultSet.getRowCount();
            // Setting items to an unfilled (but correctly sized) javascript array as data fetching is actually done in the column renderer
            node.setItems(JavaScriptObject.createArray(rowCount));
            node.setSize(rowCount);
            node.refreshItems();
            //Platform.log("GwtPolymerTable updated");
            return null;
        });
    }
}
