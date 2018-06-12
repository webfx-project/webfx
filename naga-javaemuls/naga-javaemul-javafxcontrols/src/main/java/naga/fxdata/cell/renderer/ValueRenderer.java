package naga.fxdata.cell.renderer;

import emul.javafx.scene.Node;
import naga.type.Type;
import naga.fxdata.cell.collator.NodeCollator;
import naga.fxdata.cell.collator.NodeCollatorRegistry;

/**
 * @author Bruno Salmon
 */
public interface ValueRenderer {

    Node renderValue(Object value);

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
