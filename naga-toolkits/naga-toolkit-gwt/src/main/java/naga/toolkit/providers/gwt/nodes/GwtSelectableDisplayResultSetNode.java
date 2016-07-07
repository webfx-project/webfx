package naga.toolkit.providers.gwt.nodes;

import com.google.gwt.user.client.ui.UIObject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.display.DisplaySelection;
import naga.toolkit.spi.properties.markers.SelectionMode;
import naga.toolkit.spi.nodes.SelectableDisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
public abstract class GwtSelectableDisplayResultSetNode<N extends UIObject> extends GwtDisplayResultSetNode<N> implements SelectableDisplayResultSetNode<N> {

    public GwtSelectableDisplayResultSetNode(N node) {
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
