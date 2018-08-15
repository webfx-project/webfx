package naga.framework.activity.base.elementals.presentation;

import naga.framework.activity.base.elementals.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.base.elementals.view.ViewActivityContext;
import naga.framework.activity.base.elementals.presentation.view.PresentationViewActivityContext;
import naga.framework.activity.base.composition.ComposedActivityContext;

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
