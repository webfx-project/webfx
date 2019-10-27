package webfx.extras.cell.renderer;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import webfx.extras.imagestore.ImageStore;
import webfx.extras.label.Label;
import webfx.platform.shared.util.Booleans;

/**
 * @author Bruno Salmon
 */
public final class BooleanRenderer implements ValueRenderer {

    public final static BooleanRenderer SINGLETON = new BooleanRenderer();

    private BooleanRenderer() {}

    @Override
    public Node renderValue(Object value, ValueRenderingContext context) {
        boolean booleanValue = Booleans.isTrue(value);
        // Showing the icon image instead of a combo box when there is a label with icon associated with the rendering context
        Object labelKey = context.getLabelKey();
        if (labelKey instanceof Label && context.isReadOnly()) { // TODO: add edit mode with icon
            String iconPath = ((Label) labelKey).getIconPath();
            if (iconPath != null) {
                ImageView imageView = ImageStore.createImageView(iconPath);
                imageView.setVisible(booleanValue);
                return imageView;
            }
        }
        CheckBox checkBox = new CheckBox();
        context.bindEditedValuePropertyTo(checkBox.selectedProperty(), booleanValue);
        return checkBox;
    }
}
