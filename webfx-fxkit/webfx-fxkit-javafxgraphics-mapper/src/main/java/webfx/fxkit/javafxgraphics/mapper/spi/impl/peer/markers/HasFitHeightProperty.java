package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.markers;

import javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasFitHeightProperty {

    DoubleProperty fitHeightProperty();
    default void setFitHeight(Number fitHeight) { fitHeightProperty().setValue(fitHeight); }
    default Double getFitHeight() { return fitHeightProperty().getValue(); }

}
