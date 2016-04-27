package naga.core.spi.gui.gwt.nodes;

import com.google.gwt.user.client.ui.UIObject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.gui.gwt.GwtNode;
import naga.core.spi.gui.nodes.DisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
public abstract class GwtDisplayResultSetNode<N extends UIObject> extends GwtNode<N> implements DisplayResultSetNode<N> {

    protected final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();

    public GwtDisplayResultSetNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResultSet displayResultSet);
}
