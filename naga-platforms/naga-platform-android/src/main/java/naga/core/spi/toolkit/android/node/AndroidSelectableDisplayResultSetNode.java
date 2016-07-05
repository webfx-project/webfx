package naga.core.spi.toolkit.android.node;

import android.view.View;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.spi.toolkit.propertymarkers.SelectionMode;
import naga.core.spi.toolkit.node.SelectableDisplayResultSetNode;

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
