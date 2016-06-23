package naga.core.spi.toolkit.gwt.node;

import com.google.gwt.user.client.ui.UIObject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.node.SelectableDisplayResultSetNode;

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
