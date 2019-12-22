package webfx.framework.client.activity.impl.elementals.presentation.impl;

import webfx.framework.client.activity.impl.elementals.presentation.logic.PresentationLogicActivityContext;
import webfx.framework.client.activity.impl.elementals.view.impl.ViewActivityContextBase;
import webfx.framework.client.activity.impl.elementals.presentation.PresentationActivityContext;
import webfx.framework.client.activity.impl.elementals.presentation.view.PresentationViewActivityContext;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class PresentationActivityContextBase
        <THIS extends PresentationActivityContextBase<THIS, C1, C2, PM>,
                C1 extends PresentationViewActivityContext<C1, PM>,
                C2 extends PresentationLogicActivityContext<C2, PM>,
                PM>

       extends ViewActivityContextBase<THIS>
        implements PresentationActivityContext<THIS, C1, C2, PM> {

    private C1 activityContext1;
    private C2 activityContext2;

    public PresentationActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

    @Override
    public C1 getActivityContext1() {
        return activityContext1;
    }

    @Override
    public void setActivityContext1(C1 activityContext1) {
        this.activityContext1 = activityContext1;
    }

    @Override
    public C2 getActivityContext2() {
        return activityContext2;
    }

    @Override
    public void setActivityContext2(C2 activityContext2) {
        this.activityContext2 = activityContext2;
    }

/*
    private JsonObject params;
    @Override
    public JsonObject getParams() {
        if (params == null) {
            params = Json.createObject();
            UiRouteActivityContextBase.toUiRouterActivityContextBase(getActivityContext1()).setParams(params);
            UiRouteActivityContextBase.toUiRouterActivityContextBase(getActivityContext2()).setParams(params);
        }
        return params;
    }
*/
}
