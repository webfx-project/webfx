package webfx.extras.cell.renderer;

import webfx.extras.cell.collator.NodeCollator;
import webfx.platform.shared.util.Arrays;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @author Bruno Salmon
 */
public final class ArrayRenderer implements ValueRenderer {

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
    public Node renderValue(Object value, ValueRenderingContext context) {
        return renderValue(value, valueRenderers, collator, context);
    }

    public static Node renderValue(Object value, ValueRenderer[] valueRenderers, NodeCollator collator) {
        return renderValue(value, valueRenderers, collator, ValueRenderingContext.DEFAULT_READONLY_CONTEXT);
    }

    public static Node renderValue(Object value, ValueRenderer[] valueRenderers, NodeCollator collator, ValueRenderingContext context) {
        Node[] nodes = null;
        if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            int n = Math.min(Arrays.length(array), Arrays.length(valueRenderers));
            nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                Node node = nodes[i] = valueRenderers[i].renderValue(array[i], context);
                if (node != null)
                    HBox.setHgrow(maximizeNode(node), Priority.ALWAYS);
            }
        }
        return maximizeNode(collator.collateNodes(nodes));
    }

    private static Node maximizeNode(Node node) {
/*
        if (node instanceof HasMaxWidthProperty)
            ((HasMaxWidthProperty) node).setMaxWidth(Double.MAX_VALUE);
        if (node instanceof HasMaxHeightProperty)
            ((HasMaxHeightProperty) node).setMaxHeight(Double.MAX_VALUE);
*/
        return node;
    }
}
