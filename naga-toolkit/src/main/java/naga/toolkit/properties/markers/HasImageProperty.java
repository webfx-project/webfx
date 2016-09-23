package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.spi.nodes.controls.Image;

/**
 * @author Bruno Salmon
 */
public interface HasImageProperty {

    Property<Image> imageProperty();
    default void setImage(Image text) { imageProperty().setValue(text); }
    default Image getImage() { return imageProperty().getValue(); }

}
