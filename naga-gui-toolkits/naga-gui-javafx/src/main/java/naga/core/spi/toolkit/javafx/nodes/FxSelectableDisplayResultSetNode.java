package naga.core.spi.toolkit.javafx.nodes;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import naga.core.ngui.displayselection.DisplaySelection;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.nodes.SelectableDisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
abstract class FxSelectableDisplayResultSetNode<N extends Node> extends FxDisplayResultSetNode<N> implements SelectableDisplayResultSetNode<N> {

    FxSelectableDisplayResultSetNode(N node) {
        super(node);
        syncVisualSelectionMode(getSelectionMode());
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
