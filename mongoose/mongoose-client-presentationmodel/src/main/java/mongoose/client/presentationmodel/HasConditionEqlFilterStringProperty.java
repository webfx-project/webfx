package mongoose.client.presentationmodel;

import javafx.beans.property.StringProperty;

public interface HasConditionEqlFilterStringProperty {

    StringProperty conditionEqlFilterStringProperty();
    default String getConditionEqlFilterString() { return conditionEqlFilterStringProperty().get();}
    default void setConditionEqlFilterString(String value) { conditionEqlFilterStringProperty().set(value);}

}
