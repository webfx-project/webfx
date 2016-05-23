package naga.core.spi.toolkit.gwt.nodes;

import com.google.gwt.user.client.ui.UIObject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.gwt.GwtNode;
import naga.core.spi.toolkit.nodes.DisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
public abstract class GwtDisplayResultSetNode<N extends UIObject> extends GwtNode<N> implements DisplayResultSetNode<N> {

    protected final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();

    public GwtDisplayResultSetNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> {
            try {
                onNextDisplayResult(newValue);
            } catch (Throwable e) {
                Platform.log("Exception while calling GwtDisplayResultSetNode.onNextDisplayResult(): " + e.getClass(), e);
            }
        });
    }

    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResultSet displayResultSet);
}
