package naga.providers.toolkit.gwtpolymer.nodes.controls;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.*;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.elemental.Function;
import com.vaadin.polymer.vaadin.*;
import com.vaadin.polymer.vaadin.widget.VaadinGrid;
import naga.commons.type.ArrayType;
import naga.commons.type.Type;
import naga.commons.type.Types;
import naga.commons.util.Arrays;
import naga.commons.util.Strings;
import naga.providers.toolkit.gwt.nodes.GwtSelectableDisplayResultSetNode;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.display.Label;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.nodes.controls.Table;

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
            Header header = node.getHeader();
            header.setRowCount(1);
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
                    HeaderAndFooterCell headerCell = header.getCell(0, gridColumnIndex);
                    Label label = displayColumn.getLabel();
                    SpanElement headerText = null;
                    if (label.getText() != null) {
                        headerText = Document.get().createSpanElement();
                        headerText.appendChild(Document.get().createTextNode(label.getText()));
                    }
                    ImageElement headerIcon = null;
                    if (label.getIconPath() != null) {
                        headerIcon = Document.get().createImageElement();
                        headerIcon.setSrc(label.getIconPath());
                    }
                    Node headerContent;
                    if (headerText == null) {
                        if (headerIcon != null)
                            headerIcon.setAttribute("style", "margin-left: auto; margin-right: auto;");
                        headerContent = headerIcon;
                    } else if (headerIcon == null) {
                        headerText.setAttribute("style", "margin-left: auto; margin-right: auto;");
                        headerContent = headerText;
                    } else {
                        headerIcon.setAttribute("style", "vertical-align: middle; margin-right: 5px;");
                        headerText.setAttribute("style", "vertical-align: middle;");
                        SpanElement span = Document.get().createSpanElement();
                        span.setAttribute("style", "width: 100%; text-align: center;");
                        span.appendChild(headerIcon);
                        span.appendChild(headerText);
                        headerContent = span;
                    }
                    headerCell.setContent(headerContent);
                    Double prefWidth = displayColumn.getPrefWidth();
                    if (prefWidth != null) {
                        if (label.getText() != null)
                            prefWidth = prefWidth * 2.75; // factor compared to JavaFx style (temporary hardcoded)
                        prefWidth = prefWidth + 10; // because of the 5px left and right padding
                        gridColumn.setMinWidth(prefWidth);
                        gridColumn.setMaxWidth(prefWidth);
                        gridColumn.setWidth(prefWidth);
                    }
                    final int colIndex = columnIndex;
                    Type type = displayColumn.getType();
                    boolean isArray = Types.isArrayType(type);
                    boolean isImageAndText;
                    if (isArray) {
                        Type[] types = ((ArrayType) type).getTypes();
                        isImageAndText = Arrays.length(types) == 2 && Types.isImageType(types[0]);
                    } else
                        isImageAndText = false;
                    boolean isImage = !isArray && Types.isImageType(type);
                    gridColumn.setRenderer(oCell -> {
                        Cell cell = (Cell) oCell;
                        Row row = cell.getRow().cast();
                        int rowIndex = (int) row.getIndex();
                        Object value = rs.getValue(rowIndex, colIndex);
                        String innerHtml;
                        if (isImage)
                            innerHtml = value == null ? null : "<img src='" + Strings.toString(value) + "' style='margin-left: auto; margin-right: auto;'/>";
                        else if (isImageAndText) {
                            Object[] array = (Object[]) value;
                            innerHtml = Arrays.length(array) < 2 ? null : "<span><img src='" + Strings.toString(array[0]) + "' style='vertical-align: middle; margin-right: 5px;'/><span style='vertical-align: middle;'>" + Strings.toString(array[1]) + "</span></span>";
                        } else if (isArray) {
                            innerHtml = null; // TODO
                        } else { // default case (presumably text)
                            String style = "overflow: hidden; text-overflow: ellipsis; width: 100%;";
                            if (displayColumn.getTextAlign() != null)
                                style += " text-align: " + displayColumn.getTextAlign() + ";";
                            innerHtml = value == null ? null : "<span style='" + style + "'>" + Strings.toString(value) + "</span>";
                        }
                        cell.getElement().<Element>cast().setInnerHTML(innerHtml);
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
