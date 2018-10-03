package webfx.framework.client.activity.impl.elementals.activeproperty.impl;

import javafx.beans.property.Property;
import webfx.framework.client.activity.impl.elementals.activeproperty.ActivePropertyActivity;
import webfx.framework.client.activity.impl.elementals.activeproperty.ActivePropertyActivityContext;
import webfx.framework.client.activity.impl.elementals.activeproperty.ActivePropertyActivityContextMixin;
import webfx.framework.client.activity.impl.ActivityBase;

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
