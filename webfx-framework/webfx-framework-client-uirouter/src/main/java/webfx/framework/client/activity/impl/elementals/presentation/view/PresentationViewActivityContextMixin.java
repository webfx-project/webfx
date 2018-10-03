package webfx.framework.client.activity.impl.elementals.presentation.view;

import webfx.framework.client.activity.ActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface PresentationViewActivityContextMixin
       <C extends PresentationViewActivityContext<C, PM>, PM>

       extends ActivityContextMixin<C>,
        PresentationViewActivityContext<C, PM> {

    default PM getPresentationModel() {
        return getActivityContext().getPresentationModel();
    }

}
