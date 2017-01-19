package naga.platform.activity;

/**
 * @author Bruno Salmon
 */
public class ActivityContextExtendable<THIS extends ActivityContextExtendable<THIS>> implements ActivityContext<THIS> {

    private final ActivityContext parentContext;
    private ActivityManager<THIS> activityManager;
    private final ActivityContextFactory<THIS> contextFactory;

    protected ActivityContextExtendable(ActivityContext parentContext, ActivityContextFactory<THIS> contextFactory) {
        this.parentContext = parentContext;
        this.contextFactory = contextFactory;
    }

    @Override
    public ActivityContext getParentContext() {
        return parentContext;
    }

    void setActivityManager(ActivityManager<THIS> activityManager) {
        this.activityManager = activityManager;
    }

    @Override
    public ActivityManager<THIS> getActivityManager() {
        return activityManager;
    }

    @Override
    public ActivityContextFactory<THIS> getActivityContextFactory() {
        return contextFactory;
    }

    public static <C extends ActivityContext> ActivityContextExtendable from(C activityContext) {
        if (activityContext instanceof ActivityContextExtendable)
            return (ActivityContextExtendable) activityContext;
        if (activityContext instanceof HasActivityContext) // including ActivityContextMixin
            return from(((HasActivityContext) activityContext).getActivityContext());
        return null;
    }
}
