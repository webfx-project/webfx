package naga.framework.activity.presentationlogic;

import naga.framework.activity.uiroute.UiRouteActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationLogicActivityContext
       <THIS extends PresentationLogicActivityContext<THIS, PM>, PM>

       extends UiRouteActivityContext<THIS> {

    PM getPresentationModel();
}
