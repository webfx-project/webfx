package webfx.framework.activity.base.elementals.view;

import webfx.framework.activity.base.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.activity.base.elementals.view.impl.ViewActivityContextFinal;
import webfx.fxkits.core.properties.markers.HasNodeProperty;
import webfx.framework.activity.ActivityContext;

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
