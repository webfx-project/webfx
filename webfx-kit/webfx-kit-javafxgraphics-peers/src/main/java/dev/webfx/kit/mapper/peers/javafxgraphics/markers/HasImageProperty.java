package dev.webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.Property;
import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */
public interface HasImageProperty {

    Property<Image> imageProperty();
    default void setImage(Image image) { imageProperty().setValue(image); }
    default Image getImage() { return imageProperty().getValue(); }

}
