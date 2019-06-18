package mongoose.client.entities.util.filters;

import javafx.beans.property.StringProperty;

public interface HasGroupStringFilterProperty {

    StringProperty groupStringFilterProperty();
    default String getGroupStringFilter() { return groupStringFilterProperty().get();}
    default void setGroupStringFilter(String value) { groupStringFilterProperty().set(value);}

}
