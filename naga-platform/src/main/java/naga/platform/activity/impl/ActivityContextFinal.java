package naga.platform.activity.impl;

import naga.platform.activity.ActivityContext;
import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ActivityContextFinal extends ActivityContextBase<ActivityContextFinal> {

    public ActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<ActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
