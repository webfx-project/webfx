package naga.framework.activity.presentation;

import naga.framework.activity.presentation.logic.PresentationLogicActivityContext;
import naga.framework.activity.presentation.view.PresentationViewActivityContext;
import naga.platform.activity.composition.ComposedActivity;

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
