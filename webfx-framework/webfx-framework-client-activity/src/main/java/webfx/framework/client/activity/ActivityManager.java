package webfx.framework.client.activity;

import webfx.framework.client.activity.impl.ActivityContextBase;
import webfx.platform.shared.util.async.AsyncResult;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.async.Handler;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public final class ActivityManager<C extends ActivityContext<C>> {

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
        this(activity, context == null ? null: context.getActivityContextFactory());
        init(context);
    }

    private void init(C context) {
        if (context != null)
            ActivityContextBase.toActivityContextBase(context).setActivityManager((ActivityManager) this);
        this.context = context;
    }

    public C getContext() {
        return context;
    }

    public ActivityContextFactory<C> getContextFactory() {
        return contextFactory;
    }

    public Activity<C> getActivity() {
        return activity;
    }

    public Future<Void> create(C context) {
        init(context);
        return create();
    }

    public Future<Void> create() {
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
        return onNoPendingTransitTo(intentState, Future.future(), Future.future());
    }

    private Future<Void> lastPendingFuture; // mirror of the last pending transition future - an internal handler can be set on it

    private Future<Void> onNoPendingTransitTo(State intentState, Future<Void> transitFuture, Future<Void> pendingFuture) {
        synchronized (this) {
            Future<Void> waiting = lastPendingFuture;
            lastPendingFuture = pendingFuture;
            if (waiting == null)
                return transitTo(intentState, transitFuture, pendingFuture);
            // Waiting the last transition to finish before processing this one
            waiting.setHandler(ar -> {
                if (ar.failed())
                    failFutures(ar.cause(), transitFuture, pendingFuture);
                else
                    transitTo(intentState, transitFuture, pendingFuture);
            });
            return transitFuture;
        }
    }

    private Future<Void> transitTo(State intentState, Future<Void> transitFuture, Future<Void> pendingFuture) {
        synchronized (this) {
            if (intentState == currentState)
                return completeFutures(transitFuture, pendingFuture);
            State nextState;
            if (intentState.compareTo(currentState) > 0)
                nextState = State.values()[currentState.ordinal() + 1];
            else if (currentState == State.PAUSED && intentState == State.RESUMED
                || currentState == State.STOPPED && intentState == State.STARTED)
                nextState = intentState;
            else
                return failFutures("Illegal state transition", transitFuture, pendingFuture);
            onStateChanged(nextState).setHandler(new Handler<AsyncResult<Void>>() {
                @Override
                public void handle(AsyncResult<Void> result) {
                    if (result.failed())
                        failFutures(result.cause(), transitFuture, pendingFuture);
                    else if (intentState == currentState)
                        completeFutures(transitFuture, pendingFuture);
                    else
                        transitTo(intentState, Future.future(), pendingFuture).setHandler(this);
                }
            });
        }
        return transitFuture;
    }

    private Future<Void> completeFutures(Future<Void> transitFuture, Future<Void> pendingFuture) {
        transitFuture.complete();
        return syncFutures(transitFuture, pendingFuture);
    }

    private Future<Void> failFutures(String failureMessage, Future<Void> transitFuture, Future<Void> pendingFuture) {
        transitFuture.fail(failureMessage);
        return syncFutures(transitFuture, pendingFuture);
    }

    private Future<Void> failFutures(Throwable throwable, Future<Void> transitFuture, Future<Void> pendingFuture) {
        transitFuture.fail(throwable);
        return syncFutures(transitFuture, pendingFuture);
    }

    private Future<Void> syncFutures(Future<Void> transitFuture, Future<Void> pendingFuture) {
        if (transitFuture.isComplete()) {
            synchronized (this) {
                if (lastPendingFuture == pendingFuture)
                    lastPendingFuture = null;
            }
            if (!pendingFuture.isComplete())
                if (transitFuture.failed())
                    pendingFuture.fail(transitFuture.cause());
                else
                    pendingFuture.complete();
        }
        return transitFuture;
    }

    private Future<Void> onStateChanged(State newState) {
        switch (currentState = newState) {
            case CREATED:
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

    public static <C extends ActivityContext> void runActivity(Activity<C> activity, C context) {
        new ActivityManager(activity, context).run();
    }

    public static <C extends ActivityContext<C>> Factory<ActivityManager<C>> factory(Factory<Activity<C>> activityFactory, ActivityContextFactory<C> contextFactory) {
        return () -> new ActivityManager<>(activityFactory, contextFactory);
    }
}
