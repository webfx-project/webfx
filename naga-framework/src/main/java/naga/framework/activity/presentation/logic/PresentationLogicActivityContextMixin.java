package naga.framework.activity.presentation.logic;

import naga.framework.activity.uiroute.UiRouteActivityContextMixin;

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
