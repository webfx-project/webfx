package naga.core.routing.history.impl;

import naga.core.routing.history.HistoryEvent;
import naga.core.routing.history.HistoryLocation;

import java.util.Stack;

/**
 * @author Bruno Salmon
 */
public class MemoryHistory extends HistoryBase {

    protected final Stack<HistoryLocationImpl> locationStack = new Stack<>();
    protected int backOffset = 0; // offset that becomes > 0 during back navigation to indicate the current location from the top of the history stack

    private int getCurrentLocationIndex() {
        return locationStack.size() - 1 - backOffset;
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
        int requestedBackOffset = backOffset + offset;
        if (offset != 0 && requestedBackOffset >= 0) {
            int previousBackOffset = backOffset;
            backOffset = requestedBackOffset;
            HistoryLocationImpl newLocation = getCurrentLocation();
            checkBeforeUnloadThenCheckBeforeThenTransit(newLocation, HistoryEvent.POPPED).setHandler(asyncResult -> {
                if (asyncResult.failed())
                    backOffset = previousBackOffset;
            });
        }
    }

    @Override
    protected void doAcceptedPush(HistoryLocationImpl location) {
        if (backOffset > 0)
            do
                locationStack.pop();
            while (--backOffset != 0);
        locationStack.push(location);
    }

    protected void doAcceptedReplace(HistoryLocationImpl location) {
        locationStack.set(getCurrentLocationIndex(), location);
    }

}
