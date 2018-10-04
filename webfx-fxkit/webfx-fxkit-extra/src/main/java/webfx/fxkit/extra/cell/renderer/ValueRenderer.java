package webfx.fxkit.extra.cell.renderer;

import webfx.fxkit.extra.cell.collator.NodeCollator;
import webfx.fxkit.extra.cell.collator.NodeCollatorRegistry;
import webfx.fxkit.extra.type.Type;
import javafx.scene.Node;

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
