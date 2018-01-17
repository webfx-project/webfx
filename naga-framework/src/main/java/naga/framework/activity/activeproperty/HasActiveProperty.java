package naga.framework.activity.activeproperty;

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
