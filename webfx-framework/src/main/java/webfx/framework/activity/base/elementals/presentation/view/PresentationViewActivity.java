package webfx.framework.activity.base.elementals.presentation.view;

import webfx.framework.activity.base.elementals.view.ViewActivity;

/**
 * @author Bruno Salmon
 */
public interface PresentationViewActivity
        <C extends PresentationViewActivityContext<C, PM>, PM>

    extends ViewActivity<C> {
}
