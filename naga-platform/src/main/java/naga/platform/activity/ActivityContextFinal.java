package naga.platform.activity;

/**
 * @author Bruno Salmon
 */
public final class ActivityContextFinal extends ActivityContextExtendable<ActivityContextFinal> {

    public ActivityContextFinal(ActivityContext parentContext, ActivityContextFactory<ActivityContextFinal> contextFactory) {
        super(parentContext, contextFactory);
    }
}
