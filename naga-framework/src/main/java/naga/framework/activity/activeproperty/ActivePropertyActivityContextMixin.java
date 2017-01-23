package naga.framework.activity.activeproperty;

import javafx.beans.property.ReadOnlyProperty;
import naga.platform.activity.ActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface ActivePropertyActivityContextMixin
        <C extends ActivePropertyActivityContext<C>>

        extends ActivityContextMixin<C>,
        ActivePropertyActivityContext<C> {

    @Override
    default ReadOnlyProperty<Boolean> activeProperty() {
        return getActivityContext().activeProperty();
    }

}
