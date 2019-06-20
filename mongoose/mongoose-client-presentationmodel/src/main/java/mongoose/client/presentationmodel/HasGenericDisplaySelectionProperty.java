package mongoose.client.presentationmodel;

import javafx.beans.property.ObjectProperty;
import webfx.fxkit.extra.displaydata.DisplaySelection;

public interface HasGenericDisplaySelectionProperty {

    ObjectProperty<DisplaySelection> genericDisplaySelectionProperty();
    
    default DisplaySelection getGenericDisplaySelection() { return genericDisplaySelectionProperty().getValue(); }
    
    default void setGenericDisplaySelection(DisplaySelection value) { genericDisplaySelectionProperty().setValue(value); }
    
}
