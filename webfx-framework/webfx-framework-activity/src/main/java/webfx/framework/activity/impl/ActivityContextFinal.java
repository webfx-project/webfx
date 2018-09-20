package webfx.framework.activity.impl;

import webfx.framework.activity.ActivityContext;
import webfx.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ActivityContextFinal extends ActivityContextBase<ActivityContextFinal> {

    public ActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<ActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
