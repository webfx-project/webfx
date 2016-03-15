package naga.core.spi.gui.javafx.node;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.node.DisplayNode;

/**
 * @author Bruno Salmon
 */
public abstract class FxDisplayNode<N extends Node> extends FxNode<N> implements DisplayNode<N> {

    protected final Property<DisplayResult> displayResultProperty = new SimpleObjectProperty<>();

    public FxDisplayNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResult> displayResultProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResult displayResult);
}
