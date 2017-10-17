package naga.framework.activity.uiroute.impl;

import naga.util.Objects;
import naga.framework.activity.activeproperty.impl.ActivePropertyActivityBase;
import naga.framework.activity.uiroute.UiRouteActivityContext;
import naga.framework.activity.uiroute.UiRouteActivityContextMixin;

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
        fetchRouteParameters();
        Object refreshValue = getParameter("refresh");
        if (!Objects.areEquals(refreshValue, lastRefreshValue)) {
            refreshDataOnActive();
            lastRefreshValue = refreshValue;
        }
        super.onResume(); // Making active
    }

    protected void fetchRouteParameters() {
    }

    protected void refreshDataOnActive() {
    }
}
