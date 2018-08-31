package webfx.framework.activity.base.elementals.activeproperty.impl;

import javafx.beans.property.Property;
import webfx.framework.activity.base.elementals.activeproperty.ActivePropertyActivity;
import webfx.framework.activity.base.elementals.activeproperty.ActivePropertyActivityContext;
import webfx.framework.activity.base.elementals.activeproperty.ActivePropertyActivityContextMixin;
import webfx.framework.activity.base.ActivityBase;

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
