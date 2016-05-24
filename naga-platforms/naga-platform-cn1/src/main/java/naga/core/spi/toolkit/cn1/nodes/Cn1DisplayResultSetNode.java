package naga.core.spi.toolkit.cn1.nodes;

import com.codename1.ui.Component;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.toolkit.cn1.Cn1Node;
import naga.core.spi.toolkit.nodes.DisplayResultSetNode;


/**
 * @author Bruno Salmon
 */
abstract class Cn1DisplayResultSetNode<N extends Component> extends Cn1Node<N> implements DisplayResultSetNode<N> {

    Cn1DisplayResultSetNode(N node) {
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
