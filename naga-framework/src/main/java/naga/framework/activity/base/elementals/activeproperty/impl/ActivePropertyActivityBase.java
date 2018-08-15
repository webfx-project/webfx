package naga.framework.activity.base.elementals.activeproperty.impl;

import javafx.beans.property.Property;
import naga.framework.activity.base.elementals.activeproperty.ActivePropertyActivity;
import naga.framework.activity.base.elementals.activeproperty.ActivePropertyActivityContext;
import naga.framework.activity.base.elementals.activeproperty.ActivePropertyActivityContextMixin;
import naga.framework.activity.base.ActivityBase;

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
