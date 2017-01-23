package naga.framework.activity.presentation.view;

import naga.framework.activity.view.ViewActivity;

/**
 * @author Bruno Salmon
 */
public interface PresentationViewActivity
        <C extends PresentationViewActivityContext<C, PM>, PM>

    extends ViewActivity<C> {
}
