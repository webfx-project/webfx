package naga.core.activity;

import naga.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class ActivityManager {

    enum State {LAUNCHED, CREATED, STARTED, RESUMED, PAUSED, STOPPED, DESTROYED}

    private final Factory<Activity> activityFactory;
    private Activity activity;
    private State currentState = State.LAUNCHED;
    private ActivityContext context;

    private ActivityManager(Factory<Activity> activityFactory) {
        this.activityFactory = activityFactory;
    }

    private ActivityManager(Activity activity) {
        this.activityFactory = null;
        this.activity = activity;
    }

    private ActivityManager(Activity activity, ActivityContext context) {
        this(activity);
        create(context);
    }

    public void create(ActivityContext context) {
        this.context = context;
        transitTo(State.CREATED);
    }

    public void start() {
        transitTo(State.STARTED);
    }

    public void run(ActivityContext context) {
        create(context);
        run();
    }

    public void run() {
        resume();
    }

    public void resume() {
        transitTo(State.RESUMED);
    }

    public void pause() {
        transitTo(State.PAUSED);
    }

    public void stop() {
        transitTo(State.STOPPED);
    }

    public void restart() {
        transitTo(State.STARTED);
    }

    public void destroy() {
        transitTo(State.DESTROYED);
    }

    public void transitTo(State intentState) {
        State nextState = currentState;
        while (nextState != intentState) {
            nextState = null;
            if (intentState.compareTo(currentState) > 0)
                nextState = State.values()[currentState.ordinal() + 1];
            else if (currentState == State.PAUSED && intentState == State.RESUMED
                || currentState == State.STOPPED && intentState == State.STARTED)
                nextState = intentState;
            if (nextState == null)
                throw new IllegalStateException();
            onStateChanged(nextState);
        }
    }

    private void onStateChanged(State newState) {
        currentState = newState;
        switch (currentState) {
            case CREATED:
                if (context == null)
                    throw new IllegalStateException("Activity context is not set");
                if (activity == null)
                    activity = activityFactory.create();
                activity.onCreate(context);
                break;
            case STARTED: activity.onStart(); break;
            case RESUMED: activity.onResume(); break;
            case PAUSED: activity.onPause(); break;
            case STOPPED: activity.onStop(); break;
            case DESTROYED: {
                activity.onDestroy();
                activity = null;
                break;
            }
        }
    }

    public static void launchApplication(Activity application, String[] args) {
        ActivityManager.launchActivity(application, new ApplicationContext(args));
    }

    public static void launchActivity(Activity activity, ActivityContext context) {
        from(activity, context).run();
    }

    public static ActivityManager from(Activity activity, ActivityContext context) {
        return new ActivityManager(activity, context);
    }

    public static Factory<ActivityManager> factory(Factory<Activity> activityFactory) {
        return () -> new ActivityManager(activityFactory);
    }

    public static Factory<ActivityManager> factory(Activity activity) {
        return () -> new ActivityManager(activity);
    }

    public static Factory<ActivityManager> factory(Activity activity, ActivityContext context) {
        return () -> from(activity, context);
    }


}
