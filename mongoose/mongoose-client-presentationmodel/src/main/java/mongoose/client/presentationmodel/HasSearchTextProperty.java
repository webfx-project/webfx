package mongoose.client.presentationmodel;

import javafx.beans.property.StringProperty;

public interface HasSearchTextProperty {

    StringProperty searchTextProperty();
    default String getSearchText() { return searchTextProperty().get(); }
    default void setSearchText(String value) { searchTextProperty().setValue(value); }

}
