package naga.framework.activity.presentation.view;

import naga.framework.activity.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.view.ViewActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationViewActivityContext
       <THIS extends PresentationViewActivityContext<THIS, PM>, PM>

       extends PresentationLogicActivityContext<THIS, PM>,
        ViewActivityContext<THIS> {

}
