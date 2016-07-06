package naga.core.activity;

import naga.core.spi.platform.ServerPlatform;
import naga.core.util.async.AsyncResult;
import naga.core.util.async.Future;
import naga.core.util.async.Handler;
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

    public Future<Void> create(ActivityContext context) {
        ActivityContextImpl.from(context).setActivityManager(this);
        this.context = context;
        return transitTo(State.CREATED);
    }

    public Future<Void> start() {
        return transitTo(State.STARTED);
    }

    public Future<Void> run(ActivityContext context) {
        create(context);
        return run();
    }

    public Future<Void> run() {
        return resume();
    }

    public Future<Void> resume() {
        return transitTo(State.RESUMED);
    }

    public Future<Void> pause() {
        return transitTo(State.PAUSED);
    }

    public Future<Void> stop() {
        return transitTo(State.STOPPED);
    }

    public Future<Void> restart() {
        return transitTo(State.STARTED);
    }

    public Future<Void> destroy() {
        return transitTo(State.DESTROYED);
    }

    public Future<Void> transitTo(State intentState) {
        if (intentState == currentState)
            return Future.succeededFuture();
        State nextState;
        if (intentState.compareTo(currentState) > 0)
            nextState = State.values()[currentState.ordinal() + 1];
        else if (currentState == State.PAUSED && intentState == State.RESUMED
            || currentState == State.STOPPED && intentState == State.STARTED)
            nextState = intentState;
        else
            return Future.failedFuture("Illegal state transition");
        Future<Void> future = Future.future();
        onStateChanged(nextState).setHandler(new Handler<AsyncResult<Void>>() {
            @Override
            public void handle(AsyncResult<Void> result) {
                if (result.failed())
                    future.fail(result.cause());
                else if (intentState == currentState)
                    future.complete();
                else
                    transitTo(intentState).setHandler(this);
            }
        });
        return future;
    }

    private Future<Void> onStateChanged(State newState) {
        switch (currentState = newState) {
            case CREATED:
                if (context == null)
                    return Future.failedFuture("Activity context is not set");
                if (activity == null)
                    activity = activityFactory.create();
                return activity.onCreateAsync(context);
            case STARTED: return activity.onStartAsync();
            case RESUMED: return activity.onResumeAsync();
            case PAUSED: return activity.onPauseAsync();
            case STOPPED: return activity.onStopAsync();
            case DESTROYED: {
                Future<Void> future = activity.onDestroyAsync();
                if (future.succeeded())
                    activity = null;
                return future;
            }
        }
        return Future.failedFuture("Unknown state"); // Should never occur
    }

    public static void launchApplication(Activity application, String[] args) {
        runActivity(application, new ApplicationContext(args));
    }

    public static void startBusCallMicroservice() {
        startMicroservice(new BusCallMicroservice());
    }

    public static void startMicroservice(Activity microservice) {
        ServerPlatform.get().startMicroService(from(microservice, new MicroserviceContext()));
    }

    public static void runActivity(Activity activity, ActivityContext context) {
        from(activity, context).run();
    }

    public static ActivityManager from(Activity activity, ActivityContext context) {
        return new ActivityManager(activity, context);
    }

    public static Factory<ActivityManager> factory(Factory<Activity> activityFactory) {
        return () -> new ActivityManager(activityFactory);
    }

    /* unused at the moment
     public static Factory<ActivityManager> factory(Activity activity) {
        return () -> new ActivityManager(activity);
    }

    public static Factory<ActivityManager> factory(Activity activity, ActivityContext context) {
        return () -> from(activity, context);
    } */

}
