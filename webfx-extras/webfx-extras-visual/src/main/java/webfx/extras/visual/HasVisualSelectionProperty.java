package webfx.extras.visual;

import javafx.beans.property.ObjectProperty;

/**
 * @author Bruno Salmon
 */

public interface HasVisualSelectionProperty {

    ObjectProperty<VisualSelection> visualSelectionProperty();
    default void setVisualSelection(VisualSelection visualSelection) { visualSelectionProperty().setValue(visualSelection); }
    default VisualSelection getVisualSelection() { return visualSelectionProperty().getValue(); }

}
