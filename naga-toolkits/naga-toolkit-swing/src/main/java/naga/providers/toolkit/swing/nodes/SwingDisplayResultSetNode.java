package naga.providers.toolkit.swing.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.spi.nodes.DisplayResultSetNode;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
abstract class SwingDisplayResultSetNode<N extends Component> extends SwingNode<N> implements DisplayResultSetNode<N> {

    SwingDisplayResultSetNode(N node) {
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
