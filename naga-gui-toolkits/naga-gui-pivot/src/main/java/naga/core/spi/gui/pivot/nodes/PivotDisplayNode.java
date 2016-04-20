package naga.core.spi.gui.pivot.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.nodes.DisplayNode;
import naga.core.spi.gui.pivot.PivotNode;

import org.apache.pivot.wtk.Component;

/**
 * @author Bruno Salmon
 */
public abstract class PivotDisplayNode<N extends Component> extends PivotNode<N> implements DisplayNode<N> {

    protected final Property<DisplayResult> displayResultProperty = new SimpleObjectProperty<>();

    public PivotDisplayNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResult> displayResultProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResult displayResult);
}
