package naga.core.spi.toolkit.javafx.controls;

import javafx.beans.property.Property;
import javafx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
public class FxCheckBox extends FxButtonBase<CheckBox> implements naga.core.spi.toolkit.controls.CheckBox<CheckBox> {

    public FxCheckBox() {
        this(createCheckBox());
    }

    public FxCheckBox(CheckBox checkBox) {
        super(checkBox);
    }

    private static CheckBox createCheckBox() {
        return new CheckBox();
    }

    @Override
    public Property<Boolean> selectedProperty() {
        return node.selectedProperty();
    }
}
