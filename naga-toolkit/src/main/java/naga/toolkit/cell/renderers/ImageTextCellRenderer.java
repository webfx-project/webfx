package naga.toolkit.cell.renderers;

import naga.commons.util.Arrays;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Image;
import naga.toolkit.spi.nodes.controls.TextView;

/**
 * @author Bruno Salmon
 */
public class ImageTextCellRenderer<N> implements CellRenderer<N> {

    public static ImageTextCellRenderer SINGLETON = new ImageTextCellRenderer();

    private ImageTextCellRenderer() {}

    @Override
    public GuiNode<N> renderCellValue(Object value) {
        Object[] array = getAndCheckArray(value);
        return array == null ? null : Toolkit.get().createHBox(getImage(array), getTextView(array));
    }

    public Object[] getAndCheckArray(Object value) {
        Object[] array = null;
        if (value instanceof Object[]) {
            array = (Object[]) value;
            if (Arrays.length(array) != 2)
                array = null;
        }
        return array;
    }

    private String getImageUrl(Object[] array) {
        return Arrays.getString(array, 0);
    }

    public Image getImage(Object[] array) {
        return Toolkit.get().createImageOrNull(getImageUrl(array));
    }

    public String getText(Object[] array) {
        return Arrays.getString(array, 1);
    }

    private TextView getTextView(Object[] array) {
        return Toolkit.get().createTextViewOrNull(getText(array));
    }
}
