package webfx.framework.activity.base.elementals.presentation.logic.impl;

import webfx.framework.activity.base.elementals.presentation.logic.PresentationLogicActivityContext;
import webfx.framework.activity.base.elementals.uiroute.impl.UiRouteActivityContextBase;
import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.ActivityContextFactory;

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
