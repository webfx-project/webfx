package webfx.framework.activity.base.elementals.view.impl;

import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ViewActivityContextFinal extends ViewActivityContextBase<ViewActivityContextFinal> {

    public ViewActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<ViewActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
