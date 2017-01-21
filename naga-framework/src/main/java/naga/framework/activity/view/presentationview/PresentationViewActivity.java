package naga.framework.activity.view.presentationview;

import naga.framework.activity.view.ViewActivity;

/**
 * @author Bruno Salmon
 */
public interface PresentationViewActivity
        <C extends PresentationViewActivityContext<C, PM>, PM>

    extends ViewActivity<C> {
}
