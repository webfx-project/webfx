package naga.framework.activity.view.presentation;

import naga.framework.activity.presentationlogic.PresentationLogicActivityContext;
import naga.framework.activity.view.ViewActivityContext;
import naga.framework.activity.view.presentationview.PresentationViewActivityContext;
import naga.platform.activity.composition.ComposedActivityContext;

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
