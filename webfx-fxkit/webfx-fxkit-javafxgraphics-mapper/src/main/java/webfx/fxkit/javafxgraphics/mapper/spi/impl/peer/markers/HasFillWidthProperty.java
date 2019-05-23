package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasFillWidthProperty {

    Property<Boolean> fillWidthProperty();
    default void setFillWidth(Boolean fillWidth) { fillWidthProperty().setValue(fillWidth); }
    default Boolean isFillWidth() { return fillWidthProperty().getValue(); }

}
