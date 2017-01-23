package naga.framework.activity.activeproperty.impl;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.activity.activeproperty.ActivePropertyActivityContext;
import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;
import naga.platform.activity.impl.ActivityContextBase;

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
