package webfx.framework.activity.base.elementals.activeproperty.impl;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import webfx.framework.activity.base.elementals.activeproperty.ActivePropertyActivityContext;
import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.ActivityContextFactory;
import webfx.framework.activity.base.ActivityContextBase;

/**
 * @author Bruno Salmon
 */
public class ActivePropertyActivityContextBase
        <THIS extends ActivePropertyActivityContextBase<THIS>>

        extends ActivityContextBase<THIS>
        implements ActivePropertyActivityContext<THIS> {

    protected ActivePropertyActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

    private final Property<Boolean> activeProperty = new SimpleObjectProperty<>(false);
    @Override
    public ReadOnlyProperty<Boolean> activeProperty() {
        return activeProperty;
    }

}
