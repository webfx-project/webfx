package naga.core.spi.gui.cn1.nodes;

import com.codename1.ui.Component;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.gui.cn1.Cn1Node;
import naga.core.spi.gui.nodes.DisplayResultSetNode;


/**
 * @author Bruno Salmon
 */
public abstract class Cn1DisplayResultSetNode<N extends Component> extends Cn1Node<N> implements DisplayResultSetNode<N> {

    protected final Property<DisplayResultSet> displayResultProperty = new SimpleObjectProperty<>();

    public Cn1DisplayResultSetNode(N node) {
        super(node);
        displayResultProperty.addListener((observable, oldValue, newValue) -> onNextDisplayResult(newValue));
    }

    @Override
    public Property<DisplayResultSet> displayResultSetProperty() {
        return displayResultProperty;
    }

    protected abstract void onNextDisplayResult(DisplayResultSet displayResultSet);
}
