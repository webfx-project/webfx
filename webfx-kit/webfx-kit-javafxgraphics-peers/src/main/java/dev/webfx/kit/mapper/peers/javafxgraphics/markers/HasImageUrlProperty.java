package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.StringProperty;

/**
 * @author Bruno Salmon
 */
public interface HasImageUrlProperty {

    StringProperty imageUrlProperty();
    default void setImageUrl(String imageUrl) { imageUrlProperty().setValue(imageUrl); }
    default String getImageUrl() { return imageUrlProperty().getValue(); }

}
