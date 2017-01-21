package naga.framework.activity.view.presentation;

import naga.framework.activity.presentationlogic.PresentationLogicActivityContext;
import naga.framework.activity.view.presentationview.PresentationViewActivityContext;
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
