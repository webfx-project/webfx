package naga.core.spi.gui.gwtmaterial.nodes;

import com.google.gwt.user.client.ui.UIObject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.gwtmaterial.GwtNode;
import naga.core.spi.gui.nodes.DisplayNode;

/**
 * @author Bruno Salmon
 */
public abstract class GwtDisplayNode<N extends UIObject> extends GwtNode<N> implements DisplayNode<N> {

    protected final Property<DisplayResult> displayResultProperty = new SimpleObjectProperty<>();

    public GwtDisplayNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResult> displayResultProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResult displayResult);
}
