package naga.framework.activity.activeproperty;

import javafx.beans.value.ObservableValue;
import naga.platform.activity.ActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface ActivePropertyActivityContextMixin
        <C extends ActivePropertyActivityContext<C>>

        extends ActivityContextMixin<C>,
        ActivePropertyActivityContext<C> {

    @Override
    default ObservableValue<Boolean> activeProperty() {
        return getActivityContext().activeProperty();
    }

}
