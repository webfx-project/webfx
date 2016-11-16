package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.property.Property;
import javafx.scene.control.ButtonBase;
import naga.providers.toolkit.javafx.nodes.FxNode;

/**
 * @author Bruno Salmon
 */
abstract class FxButtonBase<N extends ButtonBase> extends FxNode<N> implements naga.toolkit.spi.nodes.controls.ButtonBase {

    FxButtonBase(N button) {
        super(button);
    }

    @Override
    public Property<String> textProperty() {
        return node.textProperty();
    }

}

