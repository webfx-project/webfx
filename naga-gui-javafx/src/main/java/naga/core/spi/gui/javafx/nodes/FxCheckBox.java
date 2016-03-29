package naga.core.spi.gui.javafx.nodes;

import javafx.beans.property.Property;
import javafx.scene.control.CheckBox;

/**
 * @author Bruno Salmon
 */
public class FxCheckBox extends FxButtonBase<CheckBox> implements naga.core.spi.gui.nodes.CheckBox<CheckBox> {

    public FxCheckBox() {
        this(new CheckBox());
    }

    public FxCheckBox(CheckBox checkBox) {
        super(checkBox);
    }

    @Override
    public Property<Boolean> selectedProperty() {
        return node.selectedProperty();
    }
}
