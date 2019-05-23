package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasXProperty {

    DoubleProperty xProperty();

    default void setX(Number x) {
        xProperty().setValue(x);
    }

    default Double getX() {
        return xProperty().getValue();
    }
}
