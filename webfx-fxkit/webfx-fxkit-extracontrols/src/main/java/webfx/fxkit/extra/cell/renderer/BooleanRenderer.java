package webfx.fxkit.extra.cell.renderer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import webfx.fxkit.extra.label.Label;
import webfx.fxkit.extra.util.ImageStore;
import webfx.platform.shared.util.Booleans;

/**
 * @author Bruno Salmon
 */
public final class BooleanRenderer implements ValueRenderer {

    public final static BooleanRenderer SINGLETON = new BooleanRenderer();

    private final BooleanProperty trueProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty falseProperty = new SimpleBooleanProperty(false);

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
        if (context.isReadOnly()) {
            //checkBox.setSelected(booleanValue);
            //checkBox.setDisable(true); // The problem with that is the checkbox is grayed
            checkBox.selectedProperty().bind(booleanValue ? trueProperty : falseProperty);
        } else {
            checkBox.setSelected(booleanValue);
            context.setEditedValueProperty(checkBox.selectedProperty());
        }
        return checkBox;
    }
}
