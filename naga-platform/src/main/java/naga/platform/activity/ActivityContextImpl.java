package naga.platform.activity;

/**
 * @author Bruno Salmon
 */
public class ActivityContextImpl<C extends ActivityContextImpl<C>> implements ActivityContext<C> {

    private final ActivityContext parentContext;
    private ActivityManager<C> activityManager;
    private final ActivityContextFactory<C> contextFactory;

    protected ActivityContextImpl(ActivityContext parentContext, ActivityContextFactory<C> contextFactory) {
        this.parentContext = parentContext;
        this.contextFactory = contextFactory;
    }

    @Override
    public ActivityContext getParentContext() {
        return parentContext;
    }

    void setActivityManager(ActivityManager<C> activityManager) {
        this.activityManager = activityManager;
    }

    @Override
    public ActivityManager<C> getActivityManager() {
        return activityManager;
    }

    @Override
    public ActivityContextFactory<C> getActivityContextFactory() {
        return contextFactory;
    }

    public static <C extends ActivityContext> ActivityContextImpl from(C activityContext) {
        if (activityContext instanceof ActivityContextImpl)
            return (ActivityContextImpl) activityContext;
        if (activityContext instanceof HasActivityContext) // including ActivityContextMixin
            return from(((HasActivityContext) activityContext).getActivityContext());
        return null;
    }
}
