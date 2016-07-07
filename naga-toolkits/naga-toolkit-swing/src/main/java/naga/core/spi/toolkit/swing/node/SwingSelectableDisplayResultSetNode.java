package naga.core.spi.toolkit.swing.node;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.spi.toolkit.propertymarkers.SelectionMode;
import naga.core.spi.toolkit.node.SelectableDisplayResultSetNode;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public abstract class SwingSelectableDisplayResultSetNode<N extends Component> extends SwingDisplayResultSetNode<N> implements SelectableDisplayResultSetNode<N> {

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
