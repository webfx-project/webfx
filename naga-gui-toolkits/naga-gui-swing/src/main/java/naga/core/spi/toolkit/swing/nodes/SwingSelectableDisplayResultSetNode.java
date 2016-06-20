package naga.core.spi.toolkit.swing.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.nodes.SelectableDisplayResultSetNode;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
abstract class SwingSelectableDisplayResultSetNode<N extends Component> extends SwingDisplayResultSetNode<N> implements SelectableDisplayResultSetNode<N> {

    SwingSelectableDisplayResultSetNode(N node) {
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
