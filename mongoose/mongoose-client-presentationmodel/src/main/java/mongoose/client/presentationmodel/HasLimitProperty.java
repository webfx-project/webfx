package mongoose.client.presentationmodel;

import javafx.beans.property.IntegerProperty;

public interface HasLimitProperty {

    IntegerProperty limitProperty();

    default int getLimit() { return limitProperty().get(); }

    default void setLimit(int value) { limitProperty().setValue(value);}

}
