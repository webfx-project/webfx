package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasHgapProperty {

    DoubleProperty hgapProperty();
    default void setHgap(Number hgap) { hgapProperty().setValue(hgap); }
    default Double getHgap() { return hgapProperty().getValue(); }

}
