package naga.framework.activity.uiroute.impl;

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


    @Override
    public void onResume() {
        fetchRouteParameters();
        super.onResume();
    }

    protected void fetchRouteParameters() {
    }
}
