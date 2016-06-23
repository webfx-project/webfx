package naga.core.spi.toolkit.pivot.node;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.spi.toolkit.hasproperties.SelectionMode;
import naga.core.spi.toolkit.node.SelectableDisplayResultSetNode;
import org.apache.pivot.wtk.Component;

/**
 * @author Bruno Salmon
 */
public abstract class PivotSelectableDisplayResultSetNode<N extends Component> extends PivotDisplayResultSetNode<N> implements SelectableDisplayResultSetNode<N> {

    public PivotSelectableDisplayResultSetNode(N node) {
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
