package naga.fx.properties.markers;


import emul.javafx.beans.property.StringProperty;

/**
 * @author Bruno Salmon
 */
public interface HasTextProperty {

    StringProperty textProperty();
    default void setText(String text) { textProperty().setValue(text); }
    default String getText() { return textProperty().getValue(); }

}
