package webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions;

import javafx.beans.property.ObjectProperty;
import webfx.extras.visual.VisualSelection;

public interface HasGroupVisualSelectionProperty {

    ObjectProperty<VisualSelection> groupVisualSelectionProperty();
    default VisualSelection getGroupVisualSelection() { return groupVisualSelectionProperty().get();}
    default void setGroupVisualSelection(VisualSelection value) { groupVisualSelectionProperty().set(value);}

}
