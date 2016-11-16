package naga.providers.toolkit.cn1.nodes;

import com.codename1.ui.Component;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.nodes.SelectableDisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
public abstract class Cn1SelectableDisplayResultSetNode<N extends Component> extends Cn1DisplayResultSetNode<N> implements SelectableDisplayResultSetNode {

    public Cn1SelectableDisplayResultSetNode(N node) {
        super(node);
        selectionModeProperty.addListener((observable, oldValue, newValue) -> onNextSelectionMode(newValue));
    }

    private final Property<SelectionMode> selectionModeProperty = new SimpleObjectProperty<>(SelectionMode.DISABLED);
    @Override
    public Property<SelectionMode> selectionModeProperty() {
        return selectionModeProperty;
    }

    protected abstract void onNextSelectionMode(SelectionMode selectionMode);

    private final Property<DisplaySelection> displaySelectionProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplaySelection> displaySelectionProperty() {
        return displaySelectionProperty;
    }

}
