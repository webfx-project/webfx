package naga.framework.activity.base;

import naga.framework.activity.ActivityContext;
import naga.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ActivityContextFinal extends ActivityContextBase<ActivityContextFinal> {

    public ActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<ActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
