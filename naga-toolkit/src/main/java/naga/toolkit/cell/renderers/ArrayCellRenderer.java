package naga.toolkit.cell.renderers;

import naga.commons.util.Arrays;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
class ArrayCellRenderer<N> implements CellRenderer<N> {

    private final CellRenderer[] cellRenderers;
    private final NodeArrayRenderer<N> nodeArrayRenderer;

    public ArrayCellRenderer(CellRenderer[] cellRenderers, NodeArrayRenderer<N> nodeArrayRenderer) {
        this.cellRenderers = cellRenderers;
        this.nodeArrayRenderer = nodeArrayRenderer;
    }

    @Override
    public GuiNode<N> renderCellValue(Object value) {
        GuiNode<N>[] nodes = null;
        if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            int n = Arrays.length(array);
            if (n == Arrays.length(cellRenderers)) {
                nodes = new GuiNode[n];
                for (int i = 0; i < n; i++)
                    nodes[i] = cellRenderers[i].renderCellValue(array[i]);
            }
        }
        return nodeArrayRenderer.renderNodeArray(nodes);
    }

}
