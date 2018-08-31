package webfx.framework.activity;

import webfx.framework.activity.base.ActivityContextFinal;

/**
 * @author Bruno Salmon
 */
public interface ActivityContext
        <THIS extends ActivityContext<THIS>>

        extends HasActivityContextFactory<THIS> {

    ActivityContext getParentContext();

    ActivityManager<THIS> getActivityManager();

    @Override
    ActivityContextFactory<THIS> getActivityContextFactory();

    static ActivityContextFinal create(ActivityContext parent) {
        return new ActivityContextFinal(parent, ActivityContext::create);
    }
}
