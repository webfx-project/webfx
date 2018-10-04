package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasFitHeightProperty {

    Property<Double> fitHeightProperty();
    default void setFitHeight(Double fitHeight) { fitHeightProperty().setValue(fitHeight); }
    default Double getFitHeight() { return fitHeightProperty().getValue(); }

}
