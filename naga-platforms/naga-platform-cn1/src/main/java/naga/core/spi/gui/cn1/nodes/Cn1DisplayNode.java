package naga.core.spi.gui.cn1.nodes;

import com.codename1.ui.Component;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.cn1.Cn1Node;
import naga.core.spi.gui.nodes.DisplayNode;


/**
 * @author Bruno Salmon
 */
public abstract class Cn1DisplayNode<N extends Component> extends Cn1Node<N> implements DisplayNode<N> {

    protected final Property<DisplayResult> displayResultProperty = new SimpleObjectProperty<>();

    public Cn1DisplayNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResult> displayResultProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResult displayResult);
}
