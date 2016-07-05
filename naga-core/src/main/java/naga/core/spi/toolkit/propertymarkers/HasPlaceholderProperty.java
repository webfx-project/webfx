package naga.core.spi.toolkit.propertymarkers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasPlaceholderProperty {

    Property<String> placeholderProperty();
    default void setPlaceholder(String placeholder) { placeholderProperty().setValue(placeholder); }
    default String getPlaceholder() { return placeholderProperty().getValue(); }

}
