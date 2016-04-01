package naga.core.spi.gui.hasproperties;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasPlaceholderProperty {

    Property<String> placeholderProperty();
    default void setPlaceholder(String placeholder) { placeholderProperty().setValue(placeholder); }
    default String getPlaceholder() { return placeholderProperty().getValue(); }

}
