package webfx.framework.client.activity.impl.elementals.activeproperty;

import javafx.beans.value.ObservableValue;

/**
 * @author Bruno Salmon
 */
public interface HasActiveProperty {

    ObservableValue<Boolean> activeProperty();

    default boolean isActive() {
        return activeProperty().getValue();
    }

}
