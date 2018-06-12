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
    public CheckBox renderCellValue(Object value) {
        CheckBox checkBox = new CheckBox();
        //checkBox.setSelected(Booleans.isTrue(value));
        //checkBox.setDisable(true); // The problem with that is the checkbox is grayed
        checkBox.selectedProperty().bind(Booleans.isTrue(value) ? trueProperty : falseProperty);
        return checkBox;
    }
}
