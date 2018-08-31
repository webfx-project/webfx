package webfx.framework.activity.base.elementals.presentation.view;

import webfx.framework.activity.base.elementals.presentation.logic.PresentationLogicActivityContext;
import webfx.framework.activity.base.elementals.view.ViewActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationViewActivityContext
       <THIS extends PresentationViewActivityContext<THIS, PM>, PM>

       extends PresentationLogicActivityContext<THIS, PM>,
        ViewActivityContext<THIS> {

}
