package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasFitWidthProperty {

    DoubleProperty fitWidthProperty();
    default void setFitWidth(Number fitWidth) { fitWidthProperty().setValue(fitWidth); }
    default Double getFitWidth() { return fitWidthProperty().getValue(); }

}
