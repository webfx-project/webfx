package naga.toolkit.properties.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasUrlProperty {

    Property<String> urlProperty();
    default void setUrl(String url) { urlProperty().setValue(url); }
    default String getUrl() { return urlProperty().getValue(); }

}
