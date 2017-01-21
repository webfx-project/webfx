package naga.framework.activity.view;

import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
final class ViewActivityContextFinal extends ViewActivityContextBase<ViewActivityContextFinal> {

    ViewActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<ViewActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
