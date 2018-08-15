package naga.framework.activity.base.elementals.presentation.logic;

import naga.framework.activity.base.elementals.uiroute.UiRouteActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationLogicActivityContext
       <THIS extends PresentationLogicActivityContext<THIS, PM>, PM>

       extends UiRouteActivityContext<THIS> {

    PM getPresentationModel();
}
