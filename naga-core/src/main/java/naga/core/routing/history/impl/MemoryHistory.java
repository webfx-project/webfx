package naga.core.routing.history.impl;

import naga.core.routing.history.HistoryEvent;
import naga.core.routing.history.HistoryLocation;
import naga.core.util.async.Future;
import naga.core.util.async.Handler;
import naga.core.util.function.Function;

import java.util.Stack;

/**
 * @author Bruno Salmon
 */
public class MemoryHistory extends HistoryBase {

    private final Stack<HistoryLocationImpl> locationStack = new Stack<>();
    private int backDelta = 0; // pointer that becomes > 0 during differential navigation to specify the current location from the top of the history stack

    private int getCurrentLocationIndex() {
        return locationStack.size() - 1 - backDelta;
    }

    @Override
    public HistoryLocationImpl getCurrentLocation() {
        int index = getCurrentLocationIndex();
        return index >= 0 && index < locationStack.size() ? locationStack.get(index) : null;
    }

    @Override
    public void transitionTo(HistoryLocation location) {
        int index = locationStack.indexOf(location);
        if (index > 0)
            go(index - getCurrentLocationIndex());
    }

    @Override
    public void go(int offset) {
        int requestedBackDelta = backDelta + offset;
        if (offset != 0 && requestedBackDelta >= 0) {
            int previousBackDelta = backDelta;
            backDelta = requestedBackDelta;
            HistoryLocationImpl newLocation = getCurrentLocation();
            checkAndTransit(newLocation, HistoryEvent.POPPED).setHandler(asyncResult -> {
                if (asyncResult.failed())
                    backDelta = previousBackDelta;
            });
        }
    }

    @Override
    protected void doAcceptedPush(HistoryLocationImpl location) {
        if (backDelta > 0)
            do
                locationStack.pop();
            while (--backDelta != 0);
        locationStack.push(location);
    }

    protected void doAcceptedReplace(HistoryLocationImpl location) {
        locationStack.set(getCurrentLocationIndex(), location);
    }

    private Handler<HistoryLocation> listener;
    @Override
    public void listen(Handler<HistoryLocation> listener) {
        this.listener = listener;
    }

    private Function<HistoryLocation, Future<Boolean>> beforeTransitionHook;
    @Override
    public void listenBeforeAsync(Function<HistoryLocation, Future<Boolean>> transitionHook) {
        beforeTransitionHook = transitionHook;
    }

    private Function<HistoryLocation, Boolean> beforeUnloadTransitionHook;
    @Override
    public void listenBeforeUnload(Function<HistoryLocation, Boolean> transitionHook) {
        beforeUnloadTransitionHook = transitionHook;
    }

    @Override
    protected boolean checkBeforeUnload(HistoryLocation location) {
        return beforeUnloadTransitionHook == null || !Boolean.FALSE.equals(beforeUnloadTransitionHook.apply(location));
    }

    @Override
    protected Future<Boolean> checkBeforeAsync(HistoryLocation location) {
        return beforeTransitionHook == null ? Future.succeededFuture(Boolean.TRUE) : beforeTransitionHook.apply(location);
    }

    @Override
    protected void fireLocationChanged(HistoryLocation location) {
        if (listener != null)
            listener.handle(location);
    }

}
