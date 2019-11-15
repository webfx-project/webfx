package mongoose.client.presentationmodel;

import javafx.beans.property.StringProperty;

public interface HasSelectedGroupConditionEqlFilterStringProperty {

    StringProperty selectedGroupConditionEqlFilterStringProperty();
    default String getSelectedGroupConditionEqlFilterString() { return selectedGroupConditionEqlFilterStringProperty().get(); }
    default void setSelectedGroupConditionEqlFilterString(String value) { selectedGroupConditionEqlFilterStringProperty().set(value); }

}
