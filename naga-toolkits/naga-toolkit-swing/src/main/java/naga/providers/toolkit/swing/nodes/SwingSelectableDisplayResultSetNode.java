package naga.providers.toolkit.swing.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.spi.nodes.SelectableDisplayResultSetNode;
import naga.toolkit.properties.markers.SelectionMode;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public abstract class SwingSelectableDisplayResultSetNode<N extends Component> extends SwingDisplayResultSetNode<N> implements SelectableDisplayResultSetNode {

    public SwingSelectableDisplayResultSetNode(N node) {
        super(node);
        //syncVisualSelectionMode(getSelectionMode());
        selectionModeProperty.addListener((observable, oldValue, newValue) -> syncVisualSelectionMode(newValue));
    }

    private final Property<SelectionMode> selectionModeProperty = new SimpleObjectProperty<>(SelectionMode.DISABLED);
    @Override
    public Property<SelectionMode> selectionModeProperty() {
        return selectionModeProperty;
    }

    protected abstract void syncVisualSelectionMode(SelectionMode selectionMode);

    private final Property<DisplaySelection> displaySelectionProperty = new SimpleObjectProperty<>();
    @Override
    public Property<DisplaySelection> displaySelectionProperty() {
        return displaySelectionProperty;
    }

}
