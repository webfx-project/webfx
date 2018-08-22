package naga.framework.activity.base.elementals.view;

import naga.framework.activity.base.elementals.uiroute.UiRouteActivityContext;
import naga.framework.activity.base.elementals.view.impl.ViewActivityContextFinal;
import naga.fx.properties.markers.HasNodeProperty;
import naga.framework.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface ViewActivityContext
        <THIS extends ViewActivityContext<THIS>>

        extends UiRouteActivityContext<THIS>,
        HasNodeProperty,
        HasMountNodeProperty {

    static ViewActivityContextFinal create(ActivityContext parent) {
        return new ViewActivityContextFinal(parent, ViewActivityContext::create);
    }

}
