package webfx.fxdata.cell.renderer;

import emul.javafx.beans.property.BooleanProperty;
import emul.javafx.beans.property.SimpleBooleanProperty;
import emul.javafx.scene.control.CheckBox;
import webfx.util.Booleans;

/**
 * @author Bruno Salmon
 */
public class BooleanRenderer implements ValueRenderer {

    public final static BooleanRenderer SINGLETON = new BooleanRenderer();

    private final BooleanProperty trueProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty falseProperty = new SimpleBooleanProperty(false);

    private BooleanRenderer() {}

    @Override
    public CheckBox renderValue(Object value, ValueRenderingContext context) {
        CheckBox checkBox = new CheckBox();
        boolean booleanValue = Booleans.isTrue(value);
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
