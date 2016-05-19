package naga.core.spi.toolkit.pivot.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.toolkit.nodes.DisplayResultSetNode;
import naga.core.spi.toolkit.pivot.PivotNode;

import org.apache.pivot.wtk.Component;

/**
 * @author Bruno Salmon
 */
public abstract class PivotDisplayResultSetNode<N extends Component> extends PivotNode<N> implements DisplayResultSetNode<N> {

    protected final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();

    public PivotDisplayResultSetNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResultSet displayResultSet);
}
