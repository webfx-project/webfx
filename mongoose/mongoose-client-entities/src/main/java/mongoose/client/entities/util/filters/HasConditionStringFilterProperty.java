package mongoose.client.entities.util.filters;

import javafx.beans.property.StringProperty;

public interface HasConditionStringFilterProperty {

    StringProperty conditionStringFilterProperty();
    default String getConditionStringFilter() { return conditionStringFilterProperty().get();}
    default void setConditionStringFilter(String value) { conditionStringFilterProperty().set(value);}

}
