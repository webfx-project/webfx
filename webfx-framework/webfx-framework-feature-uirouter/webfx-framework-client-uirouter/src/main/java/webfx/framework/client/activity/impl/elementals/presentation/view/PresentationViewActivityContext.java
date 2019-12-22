package webfx.framework.client.activity.impl.elementals.presentation.view;

import webfx.framework.client.activity.impl.elementals.presentation.logic.PresentationLogicActivityContext;
import webfx.framework.client.activity.impl.elementals.view.ViewActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationViewActivityContext
       <THIS extends PresentationViewActivityContext<THIS, PM>, PM>

       extends PresentationLogicActivityContext<THIS, PM>,
        ViewActivityContext<THIS> {

}
