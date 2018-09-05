package webfx.fxkits.core.properties.markers;


import javafx.beans.property.StringProperty;

/**
 * @author Bruno Salmon
 */
public interface HasTextProperty {

    StringProperty textProperty();
    default void setText(String text) { textProperty().setValue(text); }
    default String getText() { return textProperty().getValue(); }

}
