package naga.fxdata.cell.renderer;

import naga.util.Arrays;
import naga.fxdata.cell.collator.NodeCollatorRegistry;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * @author Bruno Salmon
 */
public class ImageTextRenderer implements ValueRenderer {

    public static ImageTextRenderer SINGLETON = new ImageTextRenderer();

    private ImageTextRenderer() {}

    @Override
    public Node renderCellValue(Object value) {
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
        return ImageRenderer.SINGLETON.renderCellValue(imageUrl);
    }

    public String getText(Object[] array) {
        return Arrays.getString(array, 1);
    }

    private Text getTextNode(Object[] array) {
        String text = getText(array);
        return TextRenderer.SINGLETON.renderCellValue(text);
    }
}
