package naga.framework.activity.activeproperty.impl;

import javafx.beans.property.Property;
import naga.framework.activity.activeproperty.ActivePropertyActivity;
import naga.framework.activity.activeproperty.ActivePropertyActivityContext;
import naga.framework.activity.activeproperty.ActivePropertyActivityContextMixin;
import naga.framework.activity.ActivityBase;

/**
 * @author Bruno Salmon
 */
public class ActivePropertyActivityBase
        <C extends ActivePropertyActivityContext<C>>

        extends ActivityBase<C>
        implements ActivePropertyActivity<C>,
        ActivePropertyActivityContextMixin<C> {

    @Override
    protected void setActive(boolean active) {
        super.setActive(active);
        ((Property<Boolean>) activeProperty()).setValue(active);
    }

}
