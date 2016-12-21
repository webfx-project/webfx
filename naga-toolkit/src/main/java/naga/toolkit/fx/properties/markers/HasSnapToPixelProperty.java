package naga.toolkit.fx.properties.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasSnapToPixelProperty {

    Property<Boolean> snapToPixelProperty();
    default void setSnapToPixel(Boolean snapToPixel) { snapToPixelProperty().setValue(snapToPixel); }
    default Boolean isSnapToPixel() { return snapToPixelProperty().getValue(); }

}
