package naga.core.spi.gui.swing.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.nodes.DisplayNode;
import naga.core.spi.gui.swing.SwingNode;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public abstract class SwingDisplayNode<N extends Component> extends SwingNode<N> implements DisplayNode<N> {

    protected final Property<DisplayResult> displayResultProperty = new SimpleObjectProperty<>();

    public SwingDisplayNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResult> displayResultProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResult displayResult);
}
