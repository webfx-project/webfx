package naga.fxdata.cell.renderer;

import emul.javafx.scene.Node;
import emul.javafx.scene.image.ImageView;
import emul.javafx.scene.text.Text;
import naga.util.Arrays;
import naga.fxdata.cell.collator.NodeCollatorRegistry;

/**
 * @author Bruno Salmon
 */
public class ImageTextRenderer implements ValueRenderer {

    public static ImageTextRenderer SINGLETON = new ImageTextRenderer();

    private ImageTextRenderer() {}

    @Override
    public Node renderValue(Object value, ValueRenderingContext context) {
        Object[] array = getAndCheckArray(value);
        return array == null ? null : NodeCollatorRegistry.hBoxCollator().collateNodes(getImage(array), getTextNode(array));
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

    public ImageView getImage(Object[] array) {
        String imageUrl = getImageUrl(array);
        return ImageRenderer.SINGLETON.renderValue(imageUrl, null);
    }

    public String getText(Object[] array) {
        return Arrays.getString(array, 1);
    }

    private Text getTextNode(Object[] array) {
        String text = getText(array);
        return (Text) TextRenderer.SINGLETON.renderValue(text, ValueRenderingContext.DEFAULT_READONLY_CONTEXT);
    }
}
