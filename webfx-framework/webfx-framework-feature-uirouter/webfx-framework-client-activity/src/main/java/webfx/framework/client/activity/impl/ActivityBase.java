package webfx.framework.client.activity.impl;

import webfx.framework.client.activity.Activity;
import webfx.framework.client.activity.ActivityContext;
import webfx.framework.client.activity.ActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public abstract class ActivityBase
        <C extends ActivityContext<C>>

        implements Activity<C>,
        ActivityContextMixin<C> {

    protected C activityContext;
    protected boolean active;

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
