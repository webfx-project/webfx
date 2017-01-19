package naga.platform.activity;

/**
 * @author Bruno Salmon
 */
public abstract class AbstractActivity<C extends ActivityContext<C>> implements Activity<C>, ActivityContextMixin<C> {

    protected C activityContext;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    public C getActivityContext() {
        return activityContext;
    }

    @Override
    public void onCreate(C context) {
        this.activityContext = context;
    }

    @Override
    public void onStart() {
        setActive(true);
    }

    @Override
    public void onResume() {
        setActive(true);
    }

    @Override
    public void onPause() {
        setActive(false);
    }

}
