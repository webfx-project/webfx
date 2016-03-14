package naga.core.spi.gui.node;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface TextProperty {

    Property<String> textProperty();
    default void setText(String text) { textProperty().setValue(text); }
    default String getText() { return textProperty().getValue(); }

}
