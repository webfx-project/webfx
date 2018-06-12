package naga.fxdata.cell.renderer;

import naga.type.ArrayType;
import naga.type.Type;
import naga.type.Types;
import naga.util.Arrays;
import naga.fxdata.cell.collator.NodeCollator;
import naga.fxdata.cell.collator.NodeCollatorRegistry;

/**
 * @author Bruno Salmon
 */
class ValueRendererFactoryImpl implements ValueRendererFactory {

    static ValueRendererFactoryImpl INSTANCE = new ValueRendererFactoryImpl();

    @Override
    public ValueRenderer createCellRenderer(Type type) {
        if (Types.isImageType(type)) // Case: image type
            return ImageRenderer.SINGLETON;
        if (Types.isHtmlType(type)) // Case: html type
            return HtmlTextRenderer.SINGLETON;
        if (Types.isArrayType(type)) { // Case: any array type (including image & text)
            Type[] types = ((ArrayType) type).getTypes();
            if (Arrays.length(types) == 2 && Types.isImageType(types[0])) // Case: image & text type
                return ImageTextRenderer.SINGLETON;
            // Case: any other array type
            ValueRenderer[] renderers = new ValueRenderer[types.length];
            for (int i = 0; i < types.length; i++)
                renderers[i] = createCellRenderer(types[i]);
            return new ArrayRenderer(renderers, getNodeCollator());
        }
        if (Types.isBooleanType(type))
            return BooleanRenderer.SINGLETON;
        // Text renderer is the default one
        return TextRenderer.SINGLETON;
    }

    protected NodeCollator getNodeCollator() {
        return NodeCollatorRegistry.hBoxCollator();
    }
}
