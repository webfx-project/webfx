package naga.core.spi.toolkit.gwtpolymer.controls;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.Element;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.elemental.Function;
import com.vaadin.polymer.vaadin.Cell;
import com.vaadin.polymer.vaadin.Column;
import com.vaadin.polymer.vaadin.Row;
import com.vaadin.polymer.vaadin.Selection;
import com.vaadin.polymer.vaadin.widget.VaadinGrid;
import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.gwt.node.GwtSelectableDisplayResultSetNode;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.type.Type;
import naga.core.type.Types;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public class GwtPolymerTable extends GwtSelectableDisplayResultSetNode<VaadinGrid> implements Table<VaadinGrid> {

    public GwtPolymerTable() {
        this(new VaadinGrid());
    }

    public GwtPolymerTable(VaadinGrid vaadinGrid) {
        super(vaadinGrid);
        syncVisualSelectionMode(getSelectionMode());
        vaadinGrid.setRowClassGenerator(createRowClassGenerator());
    }

    private boolean syncHandlerInstalled;
    private boolean syncingDisplaySelection;

    private void syncToolkitDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            Selection selection = node.getSelection();
            JsArrayInteger selected = selection.selected(null, 0, selection.getSize()).cast();
            int length = selected.length();
            DisplaySelection.Builder selectionBuilder = DisplaySelection.createBuilder(length);
            for (int i = 0; i < length; i++)
                selectionBuilder.addSelectedRow(selected.get(i));
            setDisplaySelection(selectionBuilder.build());
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
                displaySelection.forEachRow(row -> selection.select(row)); // Using method reference here with GWT would produce a ClassCastException (it seems GWT doesn't make the Integer -> double auto boxing)
            syncingDisplaySelection = false;
        }
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
        Polymer.ready(node.getElement(), o -> {
            String gridSelectionMode = null;
            switch (selectionMode) {
                case DISABLED:
                    gridSelectionMode = "disabled";
                    break;
                case SINGLE:
                    gridSelectionMode = "single";
                    break;
                case MULTIPLE:
                    gridSelectionMode = "multi";
                    break;
            }
            node.getSelection().setMode(gridSelectionMode);
            return null;
        });
    }

    private int rowStyleColumnIndex = -1;

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        Polymer.ready(node.getElement(), o -> {
            //Platform.log("Updating GwtPolymerTable: " + rs.getRowCount() + " rows & " + rs.getColumnCount() + " columns");
            JsArray gridColumns = node.getColumns();
            rowStyleColumnIndex = -1;
            int columnCount = rs.getColumnCount();
            int rowCount = rs.getRowCount();
            for (int columnIndex = 0, gridColumnIndex = 0; columnIndex < columnCount; columnIndex++) {
                DisplayColumn displayColumn = rs.getColumns()[columnIndex];
                String role = displayColumn.getRole();
                if ("style".equals(role))
                    rowStyleColumnIndex = columnIndex;
                else if (role == null) {
                    Column gridColumn;
                    if (gridColumnIndex < gridColumns.length())
                        gridColumn = gridColumns.get(gridColumnIndex).cast();
                    else
                        node.addColumn(gridColumn = JavaScriptObject.createObject().cast(), null);
                    String headerText = displayColumn.getName();
                    // The API says to use gridColumn.setContentHeader() but it doesn't work so using gridColumn.setName() instead
                    gridColumn.setName(Strings.replaceAll(headerText, ".", ""));
                    final int colIndex = columnIndex;
                    gridColumn.setRenderer(oCell -> {
                        Cell cell = (Cell) oCell;
                        Row row = cell.getRow().cast();
                        int rowIndex = (int) row.getIndex();
                        Object value = rs.getValue(rowIndex, colIndex);
                        String text = value == null ? "" : value.toString();
                        String style = "overflow: hidden; text-overflow: ellipsis; width: 100%;";
                        Type type = rs.getColumns()[colIndex].getType();
                        if (Types.isNumberType(type))
                            style += " text-align: right;";
                        cell.getElement().<Element>cast().setInnerHTML("<span style='" + style + "'>" + text + "</span>");
                        return null;
                    });
                    gridColumnIndex++;
                }
            }
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

    private Function createRowClassGenerator() {
        return oRow -> {
            DisplayResultSet rs = getDisplayResultSet();
            if (rs == null)
                return null;
            Row row = (Row) oRow;
            int rowIndex = (int) row.getIndex();
            int columnIndex = rowStyleColumnIndex;
            if (rowIndex < 0 || rowIndex >= rs.getRowCount() || columnIndex < 0 || columnIndex >= rs.getColumnCount())
                return null;
            Object value = rs.getValue(rowIndex, rowStyleColumnIndex);
            if (!(value instanceof Object[]))
                return null;
            Object[] styleClassesArray = (Object[]) value;
            StringBuilder sb = new StringBuilder();
            for (Object styleClass : styleClassesArray) {
                if (styleClass != null) {
                    if (sb.length() > 0)
                        sb.append(' ');
                    sb.append(styleClass);
                }
            }
            String styleClasses = sb.toString().trim();
            // Note: returning an empty string makes the vaadin grid run into an infinite loop!
            return styleClasses.isEmpty() ? null : styleClasses; //  Must return null instead of an empty string
        };
    }
}
