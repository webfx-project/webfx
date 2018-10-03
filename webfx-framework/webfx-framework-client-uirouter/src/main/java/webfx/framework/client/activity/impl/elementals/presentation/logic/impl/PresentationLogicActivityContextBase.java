package webfx.framework.client.activity.impl.elementals.presentation.logic.impl;

import webfx.framework.client.activity.impl.elementals.presentation.logic.PresentationLogicActivityContext;
import webfx.framework.client.activity.impl.elementals.uiroute.impl.UiRouteActivityContextBase;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class PresentationLogicActivityContextBase
        <THIS extends PresentationLogicActivityContextBase<THIS, PM>, PM>

        extends UiRouteActivityContextBase<THIS>
        implements PresentationLogicActivityContext<THIS, PM> {

    public PresentationLogicActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

    private PM presentationModel;

    @Override
    public PM getPresentationModel() {
        return presentationModel;
    }

    void setPresentationModel(PM presentationModel) {
        this.presentationModel = presentationModel;
    }

    public static <THIS extends PresentationLogicActivityContextBase<THIS, PM>, PM> THIS of(ActivityContext activityContext) {
        if (activityContext instanceof PresentationLogicActivityContextBase)
            return (THIS) activityContext;
        return null;
    }

}
