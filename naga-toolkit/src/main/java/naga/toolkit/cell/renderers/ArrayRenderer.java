package naga.toolkit.cell.renderers;

import naga.commons.util.Arrays;
import naga.toolkit.cell.collators.NodeCollator;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class ArrayRenderer implements ValueRenderer {

    private final ValueRenderer[] valueRenderers;
    private NodeCollator collator;

    public ArrayRenderer(ValueRenderer[] valueRenderers, NodeCollator collator) {
        this.valueRenderers = valueRenderers;
        this.collator = collator;
    }

    public void setCollator(NodeCollator collator) {
        this.collator = collator;
    }

    @Override
    public GuiNode renderCellValue(Object value) {
        return renderCellValue(value, valueRenderers, collator);
    }

    public static GuiNode renderCellValue(Object value, ValueRenderer[] valueRenderers, NodeCollator collator) {
        GuiNode[] nodes = null;
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
