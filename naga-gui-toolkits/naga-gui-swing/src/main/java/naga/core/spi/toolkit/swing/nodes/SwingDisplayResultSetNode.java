package naga.core.spi.toolkit.swing.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.toolkit.nodes.DisplayResultSetNode;
import naga.core.spi.toolkit.swing.SwingNode;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
abstract class SwingDisplayResultSetNode<N extends Component> extends SwingNode<N> implements DisplayResultSetNode<N> {

    SwingDisplayResultSetNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    private final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResultSet displayResultSet);
}
