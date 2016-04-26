package naga.core.spi.gui.javafx.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.gui.javafx.FxNode;
import naga.core.spi.gui.nodes.DisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
public abstract class FxDisplayResultSetNode<N extends Node> extends FxNode<N> implements DisplayResultSetNode<N> {

    protected final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();

    public FxDisplayResultSetNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResultSet displayResultSet);
}
