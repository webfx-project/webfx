package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasLayoutXProperty {

    DoubleProperty layoutXProperty();
    default void setLayoutX(Double layoutX) { layoutXProperty().setValue(layoutX); }
    default Double getLayoutX() { return layoutXProperty().getValue(); }

}
