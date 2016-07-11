package naga.platform.activity;

import naga.commons.util.async.AsyncResult;
import naga.commons.util.async.Future;
import naga.commons.util.async.Handler;
import naga.commons.util.function.Factory;
import naga.platform.bus.call.BusCallServerActivity;
import naga.platform.spi.server.ServerPlatform;

/**
 * @author Bruno Salmon
 */
public class ActivityManager<C extends ActivityContext> {

    enum State {LAUNCHED, CREATED, STARTED, RESUMED, PAUSED, STOPPED, DESTROYED}

    private final Factory<Activity<C>> activityFactory;
    private final ActivityContextFactory<C> contextFactory;
    private Activity<C> activity;
    private State currentState = State.LAUNCHED;
    private C context;

    private ActivityManager(Factory<Activity<C>> activityFactory, ActivityContextFactory<C> contextFactory) {
        this.activityFactory = activityFactory;
        this.contextFactory = contextFactory;
    }

    private ActivityManager(Activity<C> activity, ActivityContextFactory<C> contextFactory) {
        this.activityFactory = null;
        this.activity = activity;
        this.contextFactory = contextFactory;
    }

    public ActivityManager(Activity<C> activity, C context, ActivityContextFactory<C> contextFactory) {
        this(activity, contextFactory);
        init(context);
    }

    private ActivityManager(Activity<C> activity, C context) {
        this(activity, context.getActivityContextFactory());
        init(context);
    }

    private void init(C context) {
        ActivityContextImpl.from(context).setActivityManager(this);
        this.context = context;
    }

    public Future<Void> create(C context) {
        init(context);
        return transitTo(State.CREATED);
    }

    public Future<Void> start() {
        return transitTo(State.STARTED);
    }

    public Future<Void> run(C context) {
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

    /*public static <C extends ActivityContext> void launchApplication(Activity<C> application, String[] args) {
        runActivity(application, new ApplicationContext(args));
    }*/

    public static void startBusCallServerActivity() {
        startServerActivity(new BusCallServerActivity());
    }

    public static void startServerActivity(Activity serverActivity) {
        ServerPlatform.get().startServerActivity(from(serverActivity, new ServerActivityContext()));
    }

    public static <C extends ActivityContext> void runActivity(Activity<C> activity, C context) {
        from(activity, context).run();
    }

    public static <C extends ActivityContext> ActivityManager<C> from(Activity<C> activity, C context) {
        return new ActivityManager<>(activity, context);
    }

    public static <C extends ActivityContext> Factory<ActivityManager<C>> factory(Factory<Activity<C>> activityFactory, ActivityContextFactory<C> contextFactory) {
        return () -> new ActivityManager<>(activityFactory, contextFactory);
    }

}
