package naga.toolkit.properties.markers;

import javafx.beans.property.ObjectProperty;
import naga.toolkit.spi.events.MouseEvent;
import naga.toolkit.spi.events.UiEventHandler;

/**
 * @author Bruno Salmon
 */
public interface HasOnMouseClickedProperty {

    default void setOnMouseClicked(UiEventHandler<? super MouseEvent> value) {
        onMouseClickedProperty().set(value);
    }

    default UiEventHandler<? super MouseEvent> getOnMouseClicked() {
        return onMouseClickedProperty().get();
    }

    /**
     * Defines a function to be called when a mouse button has been clicked
     * (pressed and released).
     */
    ObjectProperty<UiEventHandler<? super MouseEvent>> onMouseClickedProperty();

}
