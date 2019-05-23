package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasVgapProperty {

    DoubleProperty vgapProperty();
    default void setVgap(Number vgap) { vgapProperty().setValue(vgap); }
    default Double getVgap() { return vgapProperty().getValue(); }

}
