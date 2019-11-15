package mongoose.client.presentationmodel;

import javafx.beans.property.StringProperty;

public interface HasColumnsEqlFilterStringProperty {

    StringProperty columnsEqlFilterStringProperty();
    default String getColumnsEqlFilterString() { return columnsEqlFilterStringProperty().get(); }
    default void setColumnsEqlFilterString(String value) { columnsEqlFilterStringProperty().set(value); }

}
