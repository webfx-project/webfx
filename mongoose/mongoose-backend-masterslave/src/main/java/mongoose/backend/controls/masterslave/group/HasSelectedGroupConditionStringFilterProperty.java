package mongoose.backend.controls.masterslave.group;

import javafx.beans.property.StringProperty;

public interface HasSelectedGroupConditionStringFilterProperty {

    StringProperty selectedGroupConditionStringFilterProperty();
    default String getSelectedGroupConditionStringFilter() { return selectedGroupConditionStringFilterProperty().get();}
    default void setSelectedGroupConditionStringFilter(String value) { selectedGroupConditionStringFilterProperty().set(value);}

}
