package naga.providers.toolkit.pivot.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.spi.nodes.DisplayResultSetNode;

import org.apache.pivot.wtk.Component;

/**
 * @author Bruno Salmon
 */
abstract class PivotDisplayResultSetNode<N extends Component> extends PivotNode<N> implements DisplayResultSetNode<N> {

    PivotDisplayResultSetNode(N node) {
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
