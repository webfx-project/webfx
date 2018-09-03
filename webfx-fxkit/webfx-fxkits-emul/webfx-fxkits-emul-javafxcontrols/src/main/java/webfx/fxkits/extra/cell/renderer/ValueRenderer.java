package webfx.fxkits.extra.cell.renderer;

import emul.javafx.scene.Node;
import webfx.fxkits.extra.cell.collator.NodeCollator;
import webfx.fxkits.extra.cell.collator.NodeCollatorRegistry;
import webfx.fxkits.extra.type.Type;

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
