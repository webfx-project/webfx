package naga.providers.toolkit.android.nodes;

import android.view.View;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.spi.nodes.DisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
abstract class AndroidDisplayResultSetNode<N extends View> extends AndroidNode<N> implements DisplayResultSetNode {

    AndroidDisplayResultSetNode(N node) {
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
