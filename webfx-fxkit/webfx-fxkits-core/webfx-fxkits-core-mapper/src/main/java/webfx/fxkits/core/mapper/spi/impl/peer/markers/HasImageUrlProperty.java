package webfx.fxkits.core.mapper.spi.impl.peer.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasImageUrlProperty {

    Property<String> imageUrlProperty();
    default void setImageUrl(String imageUrl) { imageUrlProperty().setValue(imageUrl); }
    default String getImageUrl() { return imageUrlProperty().getValue(); }

}
