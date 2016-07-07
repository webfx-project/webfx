package naga.toolkit.providers.gwt.nodes;

import com.google.gwt.user.client.ui.UIObject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.display.DisplayResultSet;
import naga.platform.spi.Platform;
import naga.toolkit.spi.nodes.DisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
public abstract class GwtDisplayResultSetNode<N extends UIObject> extends GwtNode<N> implements DisplayResultSetNode<N> {

    public GwtDisplayResultSetNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> {
            try {
                syncVisualDisplayResult(newValue);
            } catch (Throwable e) {
                Platform.log("Exception while calling GwtDisplayResultSetNode.syncVisualDisplayResult(): " + e.getClass(), e);
            }
        });
    }

    private final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    protected abstract void syncVisualDisplayResult(DisplayResultSet displayResultSet);
}
