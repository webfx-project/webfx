package webfx.extras.visual.controls.grid.peers.gwt.html;

import elemental2.dom.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import webfx.extras.cell.renderer.ImageTextRenderer;
import webfx.extras.label.Label;
import webfx.extras.visual.*;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.extras.visual.controls.grid.peers.base.VisualGridPeerBase;
import webfx.extras.visual.controls.grid.peers.base.VisualGridPeerMixin;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlRegionPeer;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.layoutmeasurable.HtmlLayoutMeasurable;
import webfx.kit.mapper.peers.javafxgraphics.gwt.shared.HtmlSvgNodePeer;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.DomType;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlPaints;
import webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil;
import webfx.platform.shared.util.Strings;
import webfx.platform.shared.util.tuples.Unit;

import static webfx.kit.mapper.peers.javafxgraphics.gwt.util.HtmlUtil.setStyleAttribute;

/**
 * @author Bruno Salmon
 */
public final class HtmlVisualGridPeer
        <N extends VisualGrid, NB extends VisualGridPeerBase<HTMLTableCellElement, N, NB, NM>, NM extends VisualGridPeerMixin<HTMLTableCellElement, N, NB, NM>>

        extends HtmlRegionPeer<N, NB, NM>
        implements VisualGridPeerMixin<HTMLTableCellElement, N, NB, NM>, HtmlLayoutMeasurable {


    private final HTMLTableElement table = HtmlUtil.createTableElement();
    private final HTMLTableSectionElement tHead = (HTMLTableSectionElement) table.createTHead();
    private final HTMLTableRowElement tHeadRow = (HTMLTableRowElement) tHead.insertRow(0);
    private final HTMLTableSectionElement tBody = HtmlUtil.createElement("tbody");
    private double scrollTop;

    public HtmlVisualGridPeer() {
        this((NB) new VisualGridPeerBase(), HtmlUtil.createDivElement());
    }

    public HtmlVisualGridPeer(NB base, HTMLElement element) {
        super(base, element);
        table.appendChild(tBody);
        HtmlUtil.setChild(element, table);
        setStyleAttribute(table, "width", "100%");
        // Capturing scroll position (in scrollTop field)
        element.onscroll = p0 -> {
            scrollTop = element.scrollTop;
            return null;
        };
        // Restoring scroll position when visiting back the page
        HtmlUtil.onNodeInsertedIntoDocument(element, () -> element.scrollTop = scrollTop);
    }

    @Override
    public void updateHeaderVisible(boolean headerVisible) {
        if (headerVisible)
            table.insertBefore(tHead, tBody);
        else
            table.removeChild(tHead);
    }

    @Override
    public void updateFullHeight(boolean fullHeight) {
        setElementStyleAttribute("overflow-y", fullHeight ? "hidden" : "auto");
        if (fullHeight)
            getElement().scrollTop = scrollTop = 0;
    }

    @Override
    public double prefHeight(double width) {
        setElementStyleAttribute("overflow-y", "visible");
        double height = measureHeight(width);
        setElementStyleAttribute("overflow-y", getNode().isFullHeight() ? "hidden" : "auto");
        return height;
    }

    @Override
    public void updateSelectionMode(SelectionMode mode) {
    }

    @Override
    public void updateVisualSelection(VisualSelection selection) {
        Unit<Integer> lastUnselectedRowIndex = new Unit<>(0);
        VisualGrid node = getNode();
        VisualSelection visualSelection = node.getVisualSelection();
        if (visualSelection != null)
            visualSelection.forEachRow(rowIndex -> {
                applyVisualSelectionRange(lastUnselectedRowIndex.get(), rowIndex - 1, false);
                applyVisualSelectionRange(rowIndex, rowIndex, true);
                lastUnselectedRowIndex.set(rowIndex + 1);
            });
        VisualResult rs = node.getVisualResult();
        if (rs != null)
            applyVisualSelectionRange(lastUnselectedRowIndex.get(), rs.getRowCount(), false);
    }

    private void applyVisualSelectionRange(int firstRow, int lastRow, boolean selected) {
        HTMLCollection<HTMLTableRowElement> rows = tBody.rows;
        lastRow = Math.min(lastRow, rows.getLength() - 1);
        for (int trIndex = firstRow; trIndex <= lastRow; trIndex++)
            HtmlUtil.setPseudoClass(rows.item(trIndex), "selected", selected);
    }

    @Override
    public void updateVisualResult(VisualResult rs) {
        VisualGrid node = getNode();
        node.setVisualSelection(null);
        NB base = getNodePeerBase();
        HtmlUtil.removeChildren(tHeadRow);
        HtmlUtil.removeChildren(tBody);
        base.fillGrid(rs);
        if (rs != null) {
            int rowCount = rs.getRowCount();
            int columnCount = rs.getColumnCount();
            for (int row = 0; row < rowCount; row++) {
                HTMLTableRowElement tBodyRow = (HTMLTableRowElement) tBody.insertRow(-1);
                int finalRow = row;
                tBodyRow.onmousedown = e -> {
                    MouseEvent me = (MouseEvent) e;
                    node.setVisualSelection(VisualSelection.updateRowsSelection(node.getVisualSelection(), node.getSelectionMode(), finalRow, me.button == 0, me.ctrlKey, me.shiftKey));
                    return null;
                };
                String rowStyle = base.getRowStyle(row);
                if (rowStyle != null)
                    tBodyRow.className = rowStyle;
                tBodyRow.style.background = HtmlPaints.toCssPaint(base.getRowBackground(row), DomType.HTML);
                for (int column = 0; column < columnCount; column++) {
                    if (base.isDataColumn(column))
                        base.fillCell((HTMLTableCellElement) tBodyRow.insertCell(-1), row, column);
                }
            }
        }
        clearCache();
    }

    @Override
    public void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, VisualColumn visualColumn) {
        Label label = visualColumn.getLabel();
        getNodePeerBase().fillCell((HTMLTableCellElement) tHeadRow.insertCell(gridColumnIndex), new Object[]{label.getIconPath(), label.getText()}, visualColumn, ImageTextRenderer.SINGLETON);
    }

    @Override
    public void setCellContent(HTMLTableCellElement cell, Node content, VisualColumn visualColumn) {
        VisualStyle visualStyle = visualColumn.getStyle();
        String textAlign = visualStyle.getTextAlign();
        Double prefWidth = visualStyle.getPrefWidth();
        CSSStyleDeclaration cssStyle = cell.style;
        if (prefWidth != null) {
            String prefWidthPx = toPx(prefWidth);
            cssStyle.width = CSSProperties.WidthUnionType.of(prefWidthPx); // Enough for Chrome
            cssStyle.maxWidth = CSSProperties.MaxWidthUnionType.of(prefWidthPx); // Required for FireFox
            cssStyle.tableLayout = "fixed";
            if (textAlign == null)
                textAlign = "center";
        }
        if (textAlign != null)
            cssStyle.textAlign = textAlign;
        HtmlSvgNodePeer nodePeer = content == null ? null : toNodePeer(content, getNode().getScene());
        Element contentElement =  nodePeer == null ? null : nodePeer.getContainer();
        if (contentElement != null) {
            Element visibleContainer = nodePeer.getVisibleContainer();
            setStyleAttribute(visibleContainer, "position", "relative");
            //setStyleAttribute(contentElement, "width", null);
            //setStyleAttribute(contentElement, "height", null);
            if (content instanceof HBox || content instanceof CheckBox) { // temporary code for HBox, especially for table headers
                double spacing = content instanceof HBox ? ((HBox) content).getSpacing() : 0;
                resetChildrenPositionToRelative(visibleContainer, spacing);
            } else if (content instanceof Parent) {
                if (content instanceof Region) {
                    Region region = (Region) content;
                    if (contentElement.parentNode == null)
                        contentElement.ownerDocument.documentElement.appendChild(contentElement);
                    double width = region.prefWidth(-1);
                    double height = region.prefHeight(-1);
                    region.resize(width, height);
                }
                ((Parent) content).layout();
            }
            cell.appendChild(contentElement);
        }
    }

    private void resetChildrenPositionToRelative(Element contentElement, double spacing) {
        for (int i = 0, n = contentElement.childElementCount; i < n; i++) {
            elemental2.dom.Node childNode = contentElement.childNodes.item(i);
            if (childNode instanceof HTMLImageElement && Strings.isEmpty(((HTMLImageElement) childNode).src)) {
                contentElement.removeChild(childNode);
                i--; n--;
            } else {
                // Case of an invisible container (like created by HtmlImagePeer) -> we need to apply the style attributes to the child and not this element
                if (childNode instanceof HTMLElement && "contents".equals(((HTMLElement) childNode).style.display)) {
                    childNode = childNode.firstChild;
                    if (childNode == null)
                        continue;
                }
                setStyleAttribute(childNode, "position", "relative");
                if (spacing > 0 && i < n - 1)
                    setStyleAttribute(childNode, "margin-right", toPx(spacing));
                if (childNode instanceof Element) // Added, required in case of JavaFx CheckBox to have the image centered
                    resetChildrenPositionToRelative((Element) childNode, 0);
            }
        }
    }

/*
    private final HtmlLayoutCache cache = new HtmlLayoutCache();
    @Override
    public HtmlLayoutCache getCache() {
        return cache;
    }
*/
}
