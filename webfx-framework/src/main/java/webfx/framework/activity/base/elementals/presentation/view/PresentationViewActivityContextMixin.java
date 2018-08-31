package webfx.framework.activity.base.elementals.presentation.view;

import webfx.framework.activity.ActivityContextMixin;

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
