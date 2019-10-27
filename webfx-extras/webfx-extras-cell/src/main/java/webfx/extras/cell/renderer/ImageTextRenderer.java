package webfx.extras.cell.renderer;

import webfx.extras.cell.collator.NodeCollatorRegistry;
import webfx.platform.shared.util.Arrays;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * @author Bruno Salmon
 */
public final class ImageTextRenderer implements ValueRenderer {

    public final static ImageTextRenderer SINGLETON = new ImageTextRenderer();

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
