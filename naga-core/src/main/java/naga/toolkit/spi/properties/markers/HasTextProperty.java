package naga.toolkit.spi.properties.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasTextProperty {

    Property<String> textProperty();
    default void setText(String text) { textProperty().setValue(text); }
    default String getText() { return textProperty().getValue(); }

}
