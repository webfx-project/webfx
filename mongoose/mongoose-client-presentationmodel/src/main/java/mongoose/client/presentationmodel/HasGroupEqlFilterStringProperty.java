package mongoose.client.presentationmodel;

import javafx.beans.property.StringProperty;

public interface HasGroupEqlFilterStringProperty {

    StringProperty groupEqlFilterStringProperty();
    default String getGroupEqlFilterString() { return groupEqlFilterStringProperty().get();}
    default void setGroupEqlFilterString(String value) { groupEqlFilterStringProperty().set(value);}

}
