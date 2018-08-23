package naga.fx.properties.markers;


import emul.javafx.beans.property.Property;
import emul.javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */
public interface HasImageProperty {

    Property<Image> imageProperty();
    default void setImage(Image image) { imageProperty().setValue(image); }
    default Image getImage() { return imageProperty().getValue(); }

}
