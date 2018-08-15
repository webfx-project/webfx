package naga.framework.activity.base.elementals.presentation;

import naga.framework.activity.base.elementals.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.base.elementals.presentation.view.PresentationViewActivityContext;
import naga.framework.activity.base.composition.ComposedActivity;

/**
 * @author Bruno Salmon
 */
public interface PresentationActivity
        <C extends PresentationActivityContext<C, C1, C2, PM>,
                C1 extends PresentationViewActivityContext<C1, PM>,
                C2 extends PresentationLogicActivityContext<C2, PM>,
                PM>

    extends ComposedActivity<C, C1, C2> {
}
