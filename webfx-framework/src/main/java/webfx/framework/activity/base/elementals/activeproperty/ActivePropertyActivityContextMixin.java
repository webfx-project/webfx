package webfx.framework.activity.base.elementals.activeproperty;

import javafx.beans.value.ObservableValue;
import webfx.framework.activity.ActivityContextMixin;

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
