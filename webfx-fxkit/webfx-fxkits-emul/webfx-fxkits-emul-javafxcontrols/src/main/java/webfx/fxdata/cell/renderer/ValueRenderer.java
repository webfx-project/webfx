package webfx.fxdata.cell.renderer;

import emul.javafx.scene.Node;
import webfx.type.Type;
import webfx.fxdata.cell.collator.NodeCollator;
import webfx.fxdata.cell.collator.NodeCollatorRegistry;

/**
 * @author Bruno Salmon
 */
public interface ValueRenderer {

    Node renderValue(Object value, ValueRenderingContext context);

    static ValueRenderer create(Type type) {
        return ValueRendererFactory.getDefault().createValueRenderer(type);
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
