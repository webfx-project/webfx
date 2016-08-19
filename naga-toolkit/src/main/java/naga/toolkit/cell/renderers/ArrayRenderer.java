package naga.toolkit.cell.renderers;

import naga.commons.util.Arrays;
import naga.toolkit.cell.collators.NodeCollator;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class ArrayRenderer<N> implements ValueRenderer<N> {

    private final ValueRenderer[] valueRenderers;
    private NodeCollator<N> collator;

    public ArrayRenderer(ValueRenderer[] valueRenderers, NodeCollator<N> collator) {
        this.valueRenderers = valueRenderers;
        this.collator = collator;
    }

    public void setCollator(NodeCollator<N> collator) {
        this.collator = collator;
    }

    @Override
    public GuiNode<N> renderCellValue(Object value) {
        return renderCellValue(value, valueRenderers, collator);
    }

    public static <N> GuiNode<N> renderCellValue(Object value, ValueRenderer[] valueRenderers, NodeCollator<N> collator) {
        GuiNode<N>[] nodes = null;
        if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            int n = Math.min(Arrays.length(array), Arrays.length(valueRenderers));
            nodes = new GuiNode[n];
            for (int i = 0; i < n; i++)
                nodes[i] = valueRenderers[i].renderCellValue(array[i]);
        }
        return collator.collateNodes(nodes);
    }

}
