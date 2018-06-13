package naga.fxdata.cell.renderer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.CheckBox;
import naga.util.Booleans;

/**
 * @author Bruno Salmon
 */
public class BooleanRenderer implements ValueRenderer {

    public static BooleanRenderer SINGLETON = new BooleanRenderer();

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
            context.setRenderedValueProperty(checkBox.selectedProperty());
        }
        return checkBox;
    }
}
