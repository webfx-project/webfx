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
        boolean isArray = Types.isArrayType(type);
        if (isArray) {
            Type[] types = ((ArrayType) type).getTypes();
            boolean isImageAndText = Arrays.length(types) == 2 && Types.isImageType(types[0]);
            if (isImageAndText)
                return ImageTextCellRenderer.SINGLETON;
            // TODO ArrayCellRenderer
            throw new UnsupportedOperationException();
        }
        boolean isImage = !isArray && Types.isImageType(type);
        if (isImage)
            return ImageCellRenderer.SINGLETON;
        return TextCellRenderer.SINGLETON;
    }
}
