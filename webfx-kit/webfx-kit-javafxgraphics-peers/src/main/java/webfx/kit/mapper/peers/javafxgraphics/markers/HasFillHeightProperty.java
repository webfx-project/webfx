package webfx.kit.mapper.peers.javafxgraphics.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasFillHeightProperty {

    Property<Boolean> fillHeightProperty();
    default void setFillHeight(Boolean fillHeight) { fillHeightProperty().setValue(fillHeight); }
    default Boolean isFillHeight() { return fillHeightProperty().getValue(); }

}
