package naga.toolkit.fxdata.cell.renderer;

import naga.commons.type.Type;
import naga.toolkit.fxdata.cell.collator.NodeCollator;
import naga.toolkit.fxdata.cell.collator.NodeCollatorRegistry;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface ValueRenderer {

    Node renderCellValue(Object value);

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
