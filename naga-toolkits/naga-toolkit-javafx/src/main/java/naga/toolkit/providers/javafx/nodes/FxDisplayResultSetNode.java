package naga.toolkit.providers.javafx.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import naga.toolkit.spi.display.DisplayResultSet;
import naga.toolkit.spi.nodes.DisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
abstract class FxDisplayResultSetNode<N extends Node> extends FxNode<N> implements DisplayResultSetNode<N> {

    FxDisplayResultSetNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> syncVisualDisplayResult(newValue));
    }

    private final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    protected abstract void syncVisualDisplayResult(DisplayResultSet displayResultSet);
}
