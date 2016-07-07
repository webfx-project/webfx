package naga.toolkit.providers.android.nodes;

import android.view.View;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.spi.display.DisplaySelection;
import naga.toolkit.spi.properties.markers.SelectionMode;
import naga.toolkit.spi.nodes.SelectableDisplayResultSetNode;

/**
 * @author Bruno Salmon
 */
public abstract class AndroidSelectableDisplayResultSetNode<N extends View> extends AndroidDisplayResultSetNode<N> implements SelectableDisplayResultSetNode<N> {

    public AndroidSelectableDisplayResultSetNode(N node) {
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
