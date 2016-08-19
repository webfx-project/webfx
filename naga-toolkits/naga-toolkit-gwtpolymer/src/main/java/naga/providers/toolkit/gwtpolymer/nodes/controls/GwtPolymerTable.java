package naga.providers.toolkit.gwtpolymer.nodes.controls;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.ui.UIObject;
import com.vaadin.polymer.Polymer;
import com.vaadin.polymer.elemental.Function;
import com.vaadin.polymer.vaadin.*;
import com.vaadin.polymer.vaadin.widget.VaadinGrid;
import naga.commons.util.Strings;
import naga.providers.toolkit.gwt.nodes.GwtParent;
import naga.providers.toolkit.gwt.nodes.GwtSelectableDisplayResultSetNode;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.display.Label;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.cell.GridFiller;
import naga.toolkit.cell.ImageTextGridAdapter;
import naga.toolkit.spi.nodes.GuiNode;
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

    private final GridFiller gridFiller = new GridFiller<Cell>(new ImageTextGridAdapter<Cell, UIObject>() {
        @Override
        public void setCellContent(Cell cell, GuiNode<UIObject> content, DisplayColumn displayColumn) {
            // In case the content is a GWT widget with children (GwtParent) its addition to the dom won't trigger the
            // traditional GWT attach event (because we add its element and not the widget itself as the cell is not a
            // GWT object). This can be problematic if the GwtParent is waiting for this event to add its children (like
            // a newly created GwtHBox or GwtVBox collator with pending children).
            if (content instanceof GwtParent) // So in this case
                ((GwtParent) content).onAttached(null); // we simulate the attach event to cause the children addition
            Element e = content.unwrapToNativeNode().getElement();
            e.setAttribute("style", Strings.appendToken(e.getAttribute("style"), "margin-left: auto; margin-right: auto;", "; "));
            Element cellElement = cell.getElement().cast();
            cellElement.removeAllChildren();
            cellElement.appendChild(e);
        }

        @Override
        public void setCellTextContent(Cell cell, String text, DisplayColumn displayColumn) {
            String style = "overflow: hidden; text-overflow: ellipsis; width: 100%;";
            String textAlign = displayColumn.getStyle().getTextAlign();
            if (textAlign != null)
                style += " text-align: " + textAlign + ";";
            String innerHtml = text == null ? null : "<span style='" + style + "'>" + text + "</span>";
            cell.getElement().<Element>cast().setInnerHTML(innerHtml);
        }

        @Override
        public void setCellImageAndTextContent(Cell cell, GuiNode<UIObject> image, String text, DisplayColumn displayColumn) {
            Document document = Document.get();
            SpanElement span = document.createSpanElement();
            Element img = image.unwrapToNativeNode().getElement();
            img.setAttribute("style", "vertical-align: middle; margin-right: 5px;");
            span.appendChild(img);
            SpanElement textElement = document.createSpanElement();
            textElement.setInnerText(text);
            textElement.setAttribute("style", "vertical-align: middle;");
            span.appendChild(textElement);
            Element cellElement = cell.getElement().cast();
            cellElement.removeAllChildren();
            cellElement.appendChild(span);
        }
    }) {

        private JsArray gridColumns;

        @Override
        public void fillGrid(DisplayResultSet rs) {
            gridColumns = node.getColumns();
            super.fillGrid(rs);
            // Setting items to an unfilled (but correctly sized) javascript array as data fetching is actually done in the column renderer
            int rowCount = rs.getRowCount();
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
        }

        @Override
        protected void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
            Column gridColumn;
            if (gridColumnIndex < gridColumns.length())
                gridColumn = gridColumns.get(gridColumnIndex).cast();
            else
                node.addColumn(gridColumn = JavaScriptObject.createObject().cast(), null);
            HeaderAndFooterCell headerCell = node.getHeader().getCell(0, gridColumnIndex);
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
            Double prefWidth = displayColumn.getStyle().getPrefWidth();
            if (prefWidth != null) {
                if (label.getText() != null)
                    prefWidth = prefWidth * 2.75; // factor compared to JavaFx style (temporary hardcoded)
                prefWidth = prefWidth + 10; // because of the 5px left and right padding
                gridColumn.setMinWidth(prefWidth);
                gridColumn.setMaxWidth(prefWidth);
                gridColumn.setWidth(prefWidth);
            }
            gridColumn.setRenderer(oCell -> {
                Cell cell = (Cell) oCell;
                Row row = cell.getRow().cast();
                int rowIndex = (int) row.getIndex();
                fillCell(cell, rowIndex, rsColumnIndex, displayColumn);
                return null;
            });
        }
    };

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        Polymer.ready(node.getElement(), o -> {
            gridFiller.fillGrid(rs);
            return null;
        });
    }

    private Function createRowClassGenerator() {
        return oRow -> {
            Row row = (Row) oRow;
            int rowIndex = (int) row.getIndex();
            String styleClasses = gridFiller.getRowStyle(rowIndex);
            // Note: returning an empty string makes the vaadin grid run into an infinite loop!
            return Strings.isEmpty(styleClasses) ? null : styleClasses; //  Must return null instead of an empty string
        };
    }
}
