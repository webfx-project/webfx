package naga.core.spi.gui.javafx.node;

import javafx.beans.property.Property;
import javafx.scene.control.ButtonBase;

/**
 * @author Bruno Salmon
 */
public abstract class FxButtonBase<N extends ButtonBase> extends FxNode<N> implements naga.core.spi.gui.node.ToggleButton<N> {

    public FxButtonBase(N button) {
        super(button);
    }

    @Override
    public Property<String> textProperty() {
        return node.textProperty();
    }

    @Override
    public Property<Boolean> getUserInputProperty() {
        return selectedProperty();
    }

}

