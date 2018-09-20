package webfx.framework.activity.impl.elementals.presentation.view;

import webfx.framework.activity.impl.elementals.presentation.logic.PresentationLogicActivityContext;
import webfx.framework.activity.impl.elementals.view.ViewActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationViewActivityContext
       <THIS extends PresentationViewActivityContext<THIS, PM>, PM>

       extends PresentationLogicActivityContext<THIS, PM>,
        ViewActivityContext<THIS> {

}
