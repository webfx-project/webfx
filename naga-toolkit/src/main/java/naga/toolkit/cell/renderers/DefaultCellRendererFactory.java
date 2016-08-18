package naga.toolkit.cell.renderers;

import naga.commons.type.ArrayType;
import naga.commons.type.Type;
import naga.commons.type.Types;
import naga.commons.util.Arrays;

/**
 * @author Bruno Salmon
 */
class DefaultCellRendererFactory implements CellRendererFactory {

    static DefaultCellRendererFactory INSTANCE = new DefaultCellRendererFactory();

    @Override
    public CellRenderer createCellRenderer(Type type) {
        if (Types.isImageType(type)) // Case: image type
            return ImageCellRenderer.SINGLETON;
        if (Types.isArrayType(type)) { // Case: any array type (including image & text)
            Type[] types = ((ArrayType) type).getTypes();
            if (Arrays.length(types) == 2 && Types.isImageType(types[0])) // Case: image & text type
                return ImageTextCellRenderer.SINGLETON;
            // Case: any other array type
            CellRenderer[] renderers = new CellRenderer[types.length];
            for (int i = 0; i < types.length; i++)
                renderers[i] = createCellRenderer(types[i]);
            return new ArrayCellRenderer(renderers, getNodesRenderer());
        }
        //
        return TextCellRenderer.SINGLETON;
    }

    protected NodeArrayRenderer getNodesRenderer() {
        return NodeArrayRenderer.hBoxNodeArrayRenderer();
    }
}
