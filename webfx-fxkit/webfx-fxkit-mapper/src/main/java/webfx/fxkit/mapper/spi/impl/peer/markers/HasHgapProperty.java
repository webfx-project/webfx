package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasHgapProperty {

    Property<Double> hgapProperty();
    default void setHgap(Double hgap) { hgapProperty().setValue(hgap); }
    default Double getHgap() { return hgapProperty().getValue(); }

}
