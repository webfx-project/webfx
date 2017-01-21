package naga.framework.activity.view.presentationview;

import naga.framework.activity.presentationlogic.PresentationLogicActivityContext;
import naga.framework.activity.view.ViewActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationViewActivityContext
       <THIS extends PresentationViewActivityContext<THIS, PM>, PM>

       extends PresentationLogicActivityContext<THIS, PM>,
        ViewActivityContext<THIS> {

}
