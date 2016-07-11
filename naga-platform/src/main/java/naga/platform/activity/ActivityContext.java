package naga.platform.activity;

/**
 * @author Bruno Salmon
 */
public interface ActivityContext<C extends ActivityContext<C>> extends HasActivityContextFactory<C> {

    ActivityContext getParentContext();

    ActivityManager<C> getActivityManager();

    @Override
    ActivityContextFactory<C> getActivityContextFactory();

    static ActivityContext create(ActivityContext parent) {
        return new ActivityContextImpl(parent, ActivityContext::create);
    }
}
