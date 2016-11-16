package naga.toolkit.cell.renderers;

import naga.commons.type.Type;
import naga.toolkit.cell.collators.NodeCollator;
import naga.toolkit.cell.collators.NodeCollatorRegistry;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public interface ValueRenderer {

    GuiNode renderCellValue(Object value);

    static ValueRenderer create(Type type) {
        return ValueRendererFactory.getDefault().createCellRenderer(type);
    }

    static ValueRenderer create(Type type, String collator) {
        return create(type, NodeCollatorRegistry.getCollator(collator));
    }

    static ValueRenderer create(Type type, NodeCollator collator) {
        ValueRenderer renderer = create(type);
        if (collator != null && renderer instanceof ArrayRenderer)
            ((ArrayRenderer) renderer).setCollator(collator);
        return renderer;
    }

}
