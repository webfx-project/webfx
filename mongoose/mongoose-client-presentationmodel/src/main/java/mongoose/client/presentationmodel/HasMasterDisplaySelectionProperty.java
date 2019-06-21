package mongoose.client.presentationmodel;

import javafx.beans.property.ObjectProperty;
import webfx.fxkit.extra.displaydata.DisplaySelection;

public interface HasMasterDisplaySelectionProperty {

    ObjectProperty<DisplaySelection> masterDisplaySelectionProperty();
    
    default DisplaySelection getMasterDisplaySelection() { return masterDisplaySelectionProperty().getValue(); }
    
    default void setMasterDisplaySelection(DisplaySelection value) { masterDisplaySelectionProperty().setValue(value); }
    
}
