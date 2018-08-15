package naga.framework.activity.presentation.logic.impl;

import naga.framework.activity.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.uiroute.impl.UiRouteActivityContextBase;
import naga.framework.activity.ActivityContext;
import naga.framework.activity.ActivityContextFactory;

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
