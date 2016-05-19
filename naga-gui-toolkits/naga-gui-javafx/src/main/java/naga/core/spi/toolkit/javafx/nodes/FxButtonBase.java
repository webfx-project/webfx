package naga.core.spi.toolkit.javafx.nodes;

import javafx.beans.property.Property;
import javafx.scene.control.ButtonBase;
import naga.core.spi.toolkit.javafx.FxNode;

/**
 * @author Bruno Salmon
 */
public abstract class FxButtonBase<N extends ButtonBase> extends FxNode<N> implements naga.core.spi.toolkit.nodes.ButtonBase<N> {

    public FxButtonBase(N button) {
        super(button);
    }

    @Override
    public Property<String> textProperty() {
        return node.textProperty();
    }

}

