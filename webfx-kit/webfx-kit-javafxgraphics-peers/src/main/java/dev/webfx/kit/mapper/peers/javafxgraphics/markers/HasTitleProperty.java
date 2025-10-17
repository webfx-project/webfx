package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.StringProperty;

/**
 * @author Bruno Salmon
 */
public interface HasTitleProperty {

    StringProperty titleProperty();
    default void setTitle(String text) { titleProperty().setValue(text); }
    default String getTitle() { return titleProperty().getValue(); }

}
