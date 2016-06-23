package naga.core.spi.toolkit.javafx.controls;

import javafx.beans.property.Property;
import javafx.scene.control.ButtonBase;
import naga.core.spi.toolkit.javafx.node.FxNode;

/**
 * @author Bruno Salmon
 */
abstract class FxButtonBase<N extends ButtonBase> extends FxNode<N> implements naga.core.spi.toolkit.controls.ButtonBase<N> {

    FxButtonBase(N button) {
        super(button);
    }

    @Override
    public Property<String> textProperty() {
        return node.textProperty();
    }

}

