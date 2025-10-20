package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.StringProperty;

/**
 * @author Bruno Salmon
 */
public interface HasPromptTextProperty {

    StringProperty promptTextProperty();
    default void setPromptText(String promptText) { promptTextProperty().setValue(promptText); }
    default String getPromptText() { return promptTextProperty().getValue(); }

}
