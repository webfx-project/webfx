package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */
public interface HasImageProperty {

    ObjectProperty<Image> imageProperty();
    default void setImage(Image image) { imageProperty().setValue(image); }
    default Image getImage() { return imageProperty().getValue(); }

}
