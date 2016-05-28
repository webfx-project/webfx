package naga.core.routing.history.impl;

import naga.core.routing.history.HistoryEvent;
import naga.core.routing.history.Location;
import naga.core.util.async.Future;
import naga.core.util.async.Handler;
import naga.core.util.function.Function;

import java.util.Stack;

/**
 * @author Bruno Salmon
 */
public class MemoryHistory extends HistoryBase {

    private final Stack<LocationImpl> locationStack = new Stack<>();
    private int backDelta = 0; // pointer that becomes > 0 during differential navigation to specify the current location from the top of the history stack

    private int getCurrentLocationIndex() {
        return locationStack.size() - 1 - backDelta;
    }

    @Override
    public LocationImpl getCurrentLocation() {
        int index = getCurrentLocationIndex();
        return index >= 0 && index < locationStack.size() ? locationStack.get(index) : null;
    }

    @Override
    public void transitionTo(Location location) {
        int index = locationStack.indexOf(location);
        if (index > 0)
            go(index - getCurrentLocationIndex());
    }

    @Override
    public void go(int delta) {
        int requestedTopDelta = backDelta + delta;
        if (delta != 0 && requestedTopDelta >= 0) {
            int rollBackTopDelta = backDelta;
            backDelta = requestedTopDelta;
            LocationImpl newLocation = getCurrentLocation();
            checkAndTransit(newLocation, HistoryEvent.POPPED).setHandler(asyncResult -> {
                if (asyncResult.failed())
                    backDelta = rollBackTopDelta;
            });
        }
    }


    @Override
    protected void doAcceptedPush(LocationImpl location) {
        if (backDelta > 0)
            do
                locationStack.pop();
            while (++backDelta != 0);
        locationStack.push(location);
    }

    protected void doAcceptedReplace(LocationImpl location) {
        locationStack.set(getCurrentLocationIndex(), location);
    }

    private Handler<Location> listener;
    @Override
    public void listen(Handler<Location> listener) {
        this.listener = listener;
    }

    private Function<Location, Future<Boolean>> beforeTransitionHook;
    @Override
    public void listenBeforeAsync(Function<Location, Future<Boolean>> transitionHook) {
        beforeTransitionHook = transitionHook;
    }

    private Function<Location, Boolean> beforeUnloadTransitionHook;
    @Override
    public void listenBeforeUnload(Function<Location, Boolean> transitionHook) {
        beforeUnloadTransitionHook = transitionHook;
    }

    @Override
    protected boolean checkBeforeUnload(Location location) {
        return beforeUnloadTransitionHook == null || !Boolean.FALSE.equals(beforeUnloadTransitionHook.apply(location));
    }

    @Override
    protected Future<Boolean> checkBeforeAsync(Location location) {
        return beforeTransitionHook == null ? Future.succeededFuture(Boolean.TRUE) : beforeTransitionHook.apply(location);
    }

    @Override
    protected void fireLocationChanged(Location location) {
        if (listener != null)
            listener.handle(location);
    }

}
