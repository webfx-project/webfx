package webfx.framework.activity.base.elementals.presentation;

import webfx.framework.activity.base.elementals.presentation.logic.PresentationLogicActivityContext;
import webfx.framework.activity.base.elementals.view.ViewActivityContext;
import webfx.framework.activity.base.elementals.presentation.view.PresentationViewActivityContext;
import webfx.framework.activity.base.composition.ComposedActivityContext;

/**
 * @author Bruno Salmon
 */
public interface PresentationActivityContext
        <THIS extends PresentationActivityContext<THIS, C1, C2, PM>,
                C1 extends PresentationViewActivityContext<C1, PM>,
                C2 extends PresentationLogicActivityContext<C2, PM>,
                PM>

       extends ComposedActivityContext<THIS, C1, C2>,
        ViewActivityContext<THIS> {


}
