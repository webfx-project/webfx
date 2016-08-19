package naga.toolkit.cell.renderers;

import naga.commons.type.ArrayType;
import naga.commons.type.Type;
import naga.commons.type.Types;
import naga.commons.util.Arrays;
import naga.toolkit.cell.collators.NodeCollator;
import naga.toolkit.cell.collators.NodeCollatorRegistry;

/**
 * @author Bruno Salmon
 */
class ValueRendererFactoryImpl implements ValueRendererFactory {

    static ValueRendererFactoryImpl INSTANCE = new ValueRendererFactoryImpl();

    @Override
    public ValueRenderer createCellRenderer(Type type) {
        if (Types.isImageType(type)) // Case: image type
            return ImageRenderer.SINGLETON;
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
        //
        return TextRenderer.SINGLETON;
    }

    protected NodeCollator getNodeCollator() {
        return NodeCollatorRegistry.hBoxCollator();
    }
}
