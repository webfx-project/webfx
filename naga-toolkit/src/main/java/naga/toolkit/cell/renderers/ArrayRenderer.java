package naga.toolkit.cell.renderers;

import naga.commons.util.Arrays;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
class ArrayRenderer<N> implements ValueRenderer<N> {

    private final ValueRenderer[] valueRenderers;
    private final NodeArrayRenderer<N> nodeArrayRenderer;

    public ArrayRenderer(ValueRenderer[] valueRenderers, NodeArrayRenderer<N> nodeArrayRenderer) {
        this.valueRenderers = valueRenderers;
        this.nodeArrayRenderer = nodeArrayRenderer;
    }

    @Override
    public GuiNode<N> renderCellValue(Object value) {
        GuiNode<N>[] nodes = null;
        if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            int n = Arrays.length(array);
            if (n == Arrays.length(valueRenderers)) {
                nodes = new GuiNode[n];
                for (int i = 0; i < n; i++)
                    nodes[i] = valueRenderers[i].renderCellValue(array[i]);
            }
        }
        return nodeArrayRenderer.renderNodeArray(nodes);
    }

}
