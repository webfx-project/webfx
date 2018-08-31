package webfx.framework.activity.base.elementals.uiroute.impl;

import webfx.util.Objects;
import webfx.framework.activity.base.elementals.activeproperty.impl.ActivePropertyActivityBase;
import webfx.framework.activity.base.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.activity.base.elementals.uiroute.UiRouteActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public class UiRouteActivityBase
        <C extends UiRouteActivityContext<C>>

        extends ActivePropertyActivityBase<C>
        implements UiRouteActivityContextMixin<C> {

    private Object lastRefreshValue;

    @Override
    public void onResume() {
        // Doing it each time, in case the params have changed on a later resume
        updateContextParametersFromRoute();
        updateModelFromContextParameters();
        super.onResume(); // Making active
    }

    protected void updateContextParametersFromRoute() {
    }

    protected void updateModelFromContextParameters() {
        Object refreshValue = getParameter("refresh");
        if (!Objects.areEquals(refreshValue, lastRefreshValue)) {
            refreshDataOnActive();
            lastRefreshValue = refreshValue;
        }
    }

    protected void refreshDataOnActive() {
    }
}
