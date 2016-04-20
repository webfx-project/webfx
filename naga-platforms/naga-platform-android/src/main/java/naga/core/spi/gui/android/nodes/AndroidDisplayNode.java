package naga.core.spi.gui.android.nodes;

import android.view.View;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.android.AndroidNode;
import naga.core.spi.gui.nodes.DisplayNode;

/**
 * @author Bruno Salmon
 */
public abstract class AndroidDisplayNode<N extends View> extends AndroidNode<N> implements DisplayNode<N> {

    protected final Property<DisplayResult> displayResultProperty = new SimpleObjectProperty<>();

    public AndroidDisplayNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResult> displayResultProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResult displayResult);
}
