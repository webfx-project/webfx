package webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions;

import javafx.beans.property.ObjectProperty;
import webfx.extras.visual.VisualSelection;

public interface HasGenericVisualSelectionProperty {

    ObjectProperty<VisualSelection> genericVisualSelectionProperty();
    
    default VisualSelection getGenericVisualSelection() { return genericVisualSelectionProperty().getValue(); }
    
    default void setGenericVisualSelection(VisualSelection value) { genericVisualSelectionProperty().setValue(value); }
    
}
