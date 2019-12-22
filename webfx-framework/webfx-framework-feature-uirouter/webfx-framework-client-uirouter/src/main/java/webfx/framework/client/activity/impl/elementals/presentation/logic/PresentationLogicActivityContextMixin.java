package webfx.framework.client.activity.impl.elementals.presentation.logic;

import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface PresentationLogicActivityContextMixin
       <C extends PresentationLogicActivityContext<C, PM>, PM>

       extends UiRouteActivityContextMixin<C>,
        PresentationLogicActivityContext<C, PM> {

    default PM getPresentationModel() {
        return getActivityContext().getPresentationModel();
    }
}
