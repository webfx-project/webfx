package naga.framework.activity.view.impl;

import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ViewActivityContextFinal extends ViewActivityContextBase<ViewActivityContextFinal> {

    public ViewActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<ViewActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
