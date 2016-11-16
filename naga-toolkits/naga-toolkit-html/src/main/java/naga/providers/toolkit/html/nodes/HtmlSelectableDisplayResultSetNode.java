package naga.providers.toolkit.html.nodes;

import elemental2.Node;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.nodes.SelectableDisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
public abstract class HtmlSelectableDisplayResultSetNode<N extends Node> extends HtmlDisplayResultSetNode<N> implements SelectableDisplayResultSetNode {

    public HtmlSelectableDisplayResultSetNode(N node) {
        super(node);
        selectionModeProperty.addListener((observable, oldValue, newValue) -> syncVisualSelectionMode(newValue));
    }

    private final Property<SelectionMode> selectionModeProperty = new SimpleObjectProperty<>(SelectionMode.SINGLE);
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
