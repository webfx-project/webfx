package naga.core.spi.toolkit.gwtpolymer.nodes;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.Element;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.vaadin.Cell;
import com.vaadin.polymer.vaadin.Column;
import com.vaadin.polymer.vaadin.Row;
import com.vaadin.polymer.vaadin.Selection;
import com.vaadin.polymer.vaadin.widget.VaadinGrid;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.ngui.displayselection.DisplaySelection;
import naga.core.spi.toolkit.gwt.nodes.GwtSelectableDisplayResultSetNode;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.nodes.Table;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerTable extends GwtSelectableDisplayResultSetNode<VaadinGrid> implements Table<VaadinGrid> {

    public GwtPolymerTable() {
        this(new VaadinGrid());
    }

    public GwtPolymerTable(VaadinGrid node) {
        super(node);
        syncVisualSelectionMode(getSelectionMode());
    }

    private boolean syncHandlerInstalled;
    private boolean syncingDisplaySelection;

    private void syncToolkitDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            Selection selection = node.getSelection();
            JsArrayInteger selected = selection.selected(null, 0, selection.getSize()).cast();
            int length = selected.length();
            int[] selectedRows = new int[length];
            for (int i = 0; i < length; i++)
                selectedRows[i] = selected.get(i);
            setDisplaySelection(new DisplaySelection(selectedRows));
            syncingDisplaySelection = false;
        }
    }

    private void syncVisualDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            Selection selection = node.getSelection();
            selection.clear();
            DisplaySelection displaySelection = getDisplaySelection();
            if (displaySelection != null)
                for (int i : displaySelection.getSelectedRows())
                    selection.select(i);
            syncingDisplaySelection = false;
        }
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
        Polymer.ready(node.getElement(), o -> {
            String vaadinSelectionMode = null;
            switch (selectionMode) {
                case DISABLED: vaadinSelectionMode = "disabled"; break;
                case SINGLE:   vaadinSelectionMode = "single"; break;
                case MULTIPLE: vaadinSelectionMode = "multi"; break;
            }
            node.getSelection().setMode(vaadinSelectionMode);
            return null;
        });
    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet displayResultSet) {
        Polymer.ready(node.getElement(), o -> {
            //Platform.log("Updating GwtPolymerTable: " + displayResultSet.getRowCount() + " rows & " + displayResultSet.getColumnCount() + " columns");
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
            node.setVisibleRows(rowCount); // This makes the grid height fit with the number of rows (no scroll bar)
            node.refreshItems();
            // The above code reset the selection so selection handlers are installed here when first data arrives (to consider possible initial selection made by logic)
            if (!syncHandlerInstalled && rowCount > 0) {
                syncVisualDisplaySelection();
                displaySelectionProperty().addListener((observable, oldValue, newValue) -> syncVisualDisplaySelection());
                node.addSelectedItemsChangedHandler(event -> syncToolkitDisplaySelection());
                syncHandlerInstalled = true;
            }
            //Platform.log("GwtPolymerTable updated");
            return null;
        });
    }
}
