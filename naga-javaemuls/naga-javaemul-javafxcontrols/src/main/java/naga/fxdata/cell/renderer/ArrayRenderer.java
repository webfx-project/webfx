package naga.fxdata.cell.renderer;

import emul.javafx.scene.Node;
import emul.javafx.scene.layout.HBox;
import emul.javafx.scene.layout.Priority;
import naga.util.Arrays;
import naga.fxdata.cell.collator.NodeCollator;

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
    public Node renderValue(Object value) {
        return renderValue(value, valueRenderers, collator);
    }

    public static Node renderValue(Object value, ValueRenderer[] valueRenderers, NodeCollator collator) {
        Node[] nodes = null;
        if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            int n = Math.min(Arrays.length(array), Arrays.length(valueRenderers));
            nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                Node node = nodes[i] = valueRenderers[i].renderValue(array[i]);
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
