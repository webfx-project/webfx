package naga.providers.toolkit.html.nodes.controls;

import elemental2.*;
import naga.commons.util.tuples.Unit;
import naga.providers.toolkit.html.nodes.HtmlSelectableDisplayResultSetNode;
import naga.toolkit.adapters.grid.GridFiller;
import naga.toolkit.cell.renderers.ImageTextRenderer;
import naga.toolkit.display.*;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.nodes.controls.Table;

import static naga.providers.toolkit.html.HtmlUtil.*;

/**
 * @author Bruno Salmon
 */
public class HtmlTable extends HtmlSelectableDisplayResultSetNode<HTMLTableElement> implements Table<HTMLTableElement> {

    public HtmlTable() {
        this(createTableElement());
    }

    public HtmlTable(HTMLTableElement node) {
        super(node);
        displaySelectionProperty().addListener((observable, oldValue, newValue) -> syncVisualDisplaySelection());
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
    }

    private void syncVisualDisplaySelection() {
        Unit<Integer> lastUnselectedRowIndex = new Unit<>(0);
        DisplaySelection displaySelection = getDisplaySelection();
        if (displaySelection != null)
            displaySelection.forEachRow(rowIndex -> {
                applyVisualSelectionRange(lastUnselectedRowIndex.get(), rowIndex - 1, false);
                applyVisualSelectionRange(rowIndex, rowIndex, true);
                lastUnselectedRowIndex.set(rowIndex + 1);
            });
        applyVisualSelectionRange(lastUnselectedRowIndex.get(), getDisplayResultSet().getRowCount(), false);
    }

    private void applyVisualSelectionRange(int firstRow, int lastRow, boolean selected) {
        HTMLCollection<HTMLTableRowElement> rows = node.rows;
        firstRow = firstRow + 1;
        lastRow = Math.min(lastRow + 1, (int) rows.getLength() - 1);
        for (int trIndex = firstRow; trIndex <= lastRow; trIndex++)
            setPseudoClass(rows.get(trIndex), "selected", selected);
    }

    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        removeChildren();
        setDisplaySelection(null);
        HTMLTableSectionElement tHead = (HTMLTableSectionElement) node.createTHead();
        tHeadRow = (HTMLTableRowElement) tHead.insertRow(0);
        gridFiller.fillGrid(rs);
        HTMLTableSectionElement tbody = createElement("tbody");
        node.appendChild(tbody);
        int rowCount = rs.getRowCount();
        int columnCount = rs.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            HTMLTableRowElement tBodyRow = (HTMLTableRowElement) tbody.insertRow(-1);
            int finalRowIndex = rowIndex;
            tBodyRow.onclick = a -> {
                DisplaySelection displaySelection = getDisplaySelection();
                if (displaySelection == null || displaySelection.getSelectedRow() != finalRowIndex)
                    displaySelection = DisplaySelection.createSingleRowSelection(finalRowIndex);
                else
                    displaySelection = null;
                setDisplaySelection(displaySelection);
                return null;
            };
            setPseudoClass(tBodyRow, gridFiller.getRowStyle(rowIndex));
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                if (columnIndex != gridFiller.getRowStyleColumnIndex())
                    gridFiller.fillCell(tBodyRow.insertCell(-1), rowIndex, columnIndex);
            }
        }
    }

    private HTMLTableRowElement tHeadRow;

    private final GridFiller gridFiller = new GridFiller<HTMLTableCellElement>((cell, content, displayColumn) -> {
        DisplayStyle style = displayColumn.getStyle();
        String textAlign = style.getTextAlign();
        if (textAlign != null)
            setStyle(cell, "text-align: " + textAlign);
        Double prefWidth = style.getPrefWidth();
        if (prefWidth != null) {
            if (displayColumn.getLabel().getText() != null)
                prefWidth = prefWidth * 2.75; // factor compared to JavaFx style (temporary hardcoded)
            //prefWidth = prefWidth + 10; // because of the 5px left and right padding
            appendStyle(cell, "width: " + prefWidth + "px");
        }
        cell.appendChild((Node) content.unwrapToNativeNode());
    }) {
        @Override
        protected void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
            Label label = displayColumn.getLabel();
            fillCell((HTMLTableCellElement) tHeadRow.insertCell(gridColumnIndex), new Object[]{label.getIconPath(), label.getText()}, displayColumn, ImageTextRenderer.SINGLETON);
        }
    };
}
