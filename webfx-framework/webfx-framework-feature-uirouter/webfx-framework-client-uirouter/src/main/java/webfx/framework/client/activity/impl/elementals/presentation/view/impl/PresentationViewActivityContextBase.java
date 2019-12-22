package webfx.framework.client.activity.impl.elementals.presentation.view.impl;

import webfx.framework.client.activity.impl.elementals.view.impl.ViewActivityContextBase;
import webfx.framework.client.activity.impl.elementals.presentation.view.PresentationViewActivityContext;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public class PresentationViewActivityContextBase
        <THIS extends PresentationViewActivityContextBase<THIS, PM>, PM>

        extends ViewActivityContextBase<THIS>
        implements PresentationViewActivityContext<THIS, PM> {

    private PM presentationModel;

    public PresentationViewActivityContextBase(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        super(parentContext, contextFactory);
    }

    @Override
    public PM getPresentationModel() {
        return presentationModel;
    }

    void setPresentationModel(PM presentationModel) {
        this.presentationModel = presentationModel;
    }

    public static <IC extends ActivityContext<IC>, OC extends PresentationViewActivityContextBase<OC, PM>, PM> OC toViewModelActivityContextBase(IC activityContext) {
        return from(activityContext, ac -> ac instanceof PresentationViewActivityContextBase);
    }

}
