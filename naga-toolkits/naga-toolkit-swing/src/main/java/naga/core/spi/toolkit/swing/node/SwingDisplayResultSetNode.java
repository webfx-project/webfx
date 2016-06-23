package naga.core.spi.toolkit.swing.node;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.spi.toolkit.node.DisplayResultSetNode;

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
