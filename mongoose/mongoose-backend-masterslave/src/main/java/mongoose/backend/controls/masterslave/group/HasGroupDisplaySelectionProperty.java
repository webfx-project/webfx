package mongoose.backend.controls.masterslave.group;

import javafx.beans.property.ObjectProperty;
import webfx.fxkit.extra.displaydata.DisplaySelection;

public interface HasGroupDisplaySelectionProperty {

    ObjectProperty<DisplaySelection> groupDisplaySelectionProperty();
    default DisplaySelection getGroupDisplaySelection() { return groupDisplaySelectionProperty().get();}
    default void setGroupDisplaySelection(DisplaySelection value) { groupDisplaySelectionProperty().set(value);}

}
