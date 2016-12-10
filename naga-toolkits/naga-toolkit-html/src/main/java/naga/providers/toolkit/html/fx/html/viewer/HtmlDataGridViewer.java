package naga.providers.toolkit.html.fx.html.viewer;

import elemental2.*;
import naga.commons.util.Strings;
import naga.commons.util.tuples.Unit;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.display.*;
import naga.toolkit.fx.ext.cell.renderer.ImageTextRenderer;
import naga.toolkit.fx.ext.control.DataGrid;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.layout.HBox;
import naga.toolkit.fx.spi.viewer.base.DataGridViewerBase;
import naga.toolkit.fx.spi.viewer.base.DataGridViewerMixin;
import naga.toolkit.properties.markers.SelectionMode;

import static naga.providers.toolkit.html.util.HtmlUtil.*;

/**
 * @author Bruno Salmon
 */
public class HtmlDataGridViewer
        extends HtmlRegionViewer<DataGrid, DataGridViewerBase<HTMLTableCellElement>, DataGridViewerMixin<HTMLTableCellElement>>
        implements DataGridViewerMixin<HTMLTableCellElement>, HtmlLayoutMeasurable {


    public HtmlDataGridViewer() {
        super(new DataGridViewerBase<>(), HtmlUtil.createTableElement());
    }

    @Override
    public void updateSelectionMode(SelectionMode mode) {
    }

    @Override
    public void updateDisplaySelection(DisplaySelection selection) {
        Unit<Integer> lastUnselectedRowIndex = new Unit<>(0);
        DataGrid node = getNode();
        DisplaySelection displaySelection = node.getDisplaySelection();
        if (displaySelection != null)
            displaySelection.forEachRow(rowIndex -> {
                applyVisualSelectionRange(lastUnselectedRowIndex.get(), rowIndex - 1, false);
                applyVisualSelectionRange(rowIndex, rowIndex, true);
                lastUnselectedRowIndex.set(rowIndex + 1);
            });
        DisplayResultSet rs = node.getDisplayResultSet();
        if (rs != null)
            applyVisualSelectionRange(lastUnselectedRowIndex.get(), rs.getRowCount(), false);
    }

    private void applyVisualSelectionRange(int firstRow, int lastRow, boolean selected) {
        HTMLCollection<HTMLTableRowElement> rows = ((HTMLTableElement) getElement()).rows;
        firstRow = firstRow + 1;
        lastRow = Math.min(lastRow + 1, (int) rows.getLength() - 1);
        for (int trIndex = firstRow; trIndex <= lastRow; trIndex++)
            setPseudoClass(rows.get(trIndex), "selected", selected);
    }

    private HTMLTableRowElement tHeadRow;

    @Override
    public void updateResultSet(DisplayResultSet rs) {
        removeChildren();
        DataGrid node = getNode();
        node.setDisplaySelection(null);
        HTMLTableElement table = (HTMLTableElement) getElement();
        HTMLTableSectionElement tHead = (HTMLTableSectionElement) table.createTHead();
        tHeadRow = (HTMLTableRowElement) tHead.insertRow(0);
        DataGridViewerBase<HTMLTableCellElement> base = getNodeViewerBase();
        base.fillGrid(rs);
        HTMLTableSectionElement tbody = createElement("tbody");
        table.appendChild(tbody);
        if (rs != null) {
            int rowCount = rs.getRowCount();
            int columnCount = rs.getColumnCount();
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                HTMLTableRowElement tBodyRow = (HTMLTableRowElement) tbody.insertRow(-1);
                int finalRowIndex = rowIndex;
                tBodyRow.onclick = a -> {
                    DisplaySelection displaySelection = node.getDisplaySelection();
                    if (displaySelection == null || displaySelection.getSelectedRow() != finalRowIndex)
                        displaySelection = DisplaySelection.createSingleRowSelection(finalRowIndex);
                    else
                        displaySelection = null;
                    node.setDisplaySelection(displaySelection);
                    return null;
                };
                setPseudoClass(tBodyRow, base.getRowStyle(rowIndex));
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    if (columnIndex != base.getRowStyleColumnIndex())
                        base.fillCell((HTMLTableCellElement) tBodyRow.insertCell(-1), rowIndex, columnIndex);
                }
            }
        }
    }

    @Override
    public void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
        Label label = displayColumn.getLabel();
        getNodeViewerBase().fillCell((HTMLTableCellElement) tHeadRow.insertCell(gridColumnIndex), new Object[]{label.getIconPath(), label.getText()}, displayColumn, ImageTextRenderer.SINGLETON);
    }

    @Override
    public void setCellContent(HTMLTableCellElement cell, Node content, DisplayColumn displayColumn) {
        DisplayStyle style = displayColumn.getStyle();
        String textAlign = style.getTextAlign();
        if (textAlign != null)
            setStyleAttribute(cell, "text-align", textAlign);
        Double prefWidth = style.getPrefWidth();
        if (prefWidth != null) {
            String prefWidthPx = toPx(prefWidth);
            setStyleAttribute(cell, "width", prefWidthPx);
            setStyleAttribute(cell, "table-layout", "fixed");
            setStyleAttribute(cell, "overflow", "hidden");
            if (textAlign == null)
                setStyleAttribute(cell, "text-align", "center");
        }
        Element contentViewElement = toElement(content, getNode().getDrawing());
        if (contentViewElement != null) {
            setStyleAttribute(contentViewElement, "position", "relative");
            setStyleAttribute(contentViewElement, "width", null);
            setStyleAttribute(contentViewElement, "height", null);
            cell.appendChild(contentViewElement);
            double spacing = content instanceof HBox ? ((HBox) content).getSpacing() : 0;
            for (int i = 0, n = (int) contentViewElement.childElementCount; i < n; i++) {
                elemental2.Node childNode = contentViewElement.childNodes.get(i);
                if (childNode instanceof HTMLImageElement && Strings.isEmpty(((HTMLImageElement) childNode).src)) {
                    contentViewElement.removeChild(childNode);
                    i--; n--;
                } else {
                    setStyleAttribute(childNode, "position", "relative");
                    if (spacing > 0 && i < n - 1)
                        setStyleAttribute(childNode, "margin-right", toPx(spacing));
                }
            }
        }
    }
}
