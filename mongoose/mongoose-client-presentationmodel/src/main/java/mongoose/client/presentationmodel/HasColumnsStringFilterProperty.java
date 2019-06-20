package mongoose.client.presentationmodel;

import javafx.beans.property.StringProperty;

public interface HasColumnsStringFilterProperty {

    StringProperty columnsStringFilterProperty();
    default String getColumnsStringFilter() { return columnsStringFilterProperty().get(); }
    default void setColumnsStringFilter(String value) { columnsStringFilterProperty().set(value); }

}
