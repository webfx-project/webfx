package dev.webfx.platform.client.services.windowhistory.spi.impl;

import dev.webfx.platform.client.services.windowhistory.spi.BrowsingHistoryEvent;
import dev.webfx.platform.client.services.windowhistory.spi.BrowsingHistoryLocation;

import java.util.Stack;

/**
 * @author Bruno Salmon
 */
public class MemoryBrowsingHistory extends BrowsingHistoryBase {

    protected final Stack<BrowsingHistoryLocationImpl> locationStack = new Stack<>();
    protected int backOffset = 0; // offset that becomes > 0 during back navigation to indicate the current location from the top of the history stack

    private int getCurrentLocationIndex() {
        return locationStack.size() - 1 - backOffset;
    }

    @Override
    public BrowsingHistoryLocationImpl getCurrentLocation() {
        int index = getCurrentLocationIndex();
        return index >= 0 && index < locationStack.size() ? locationStack.get(index) : null;
    }

    @Override
    public void transitionTo(BrowsingHistoryLocation location) {
        int index = locationStack.indexOf(location);
        if (index > 0)
            go(index - getCurrentLocationIndex());
    }

    @Override
    public void go(int offset) {
        int requestedBackOffset = backOffset - offset;
        if (offset != 0 && requestedBackOffset >= 0 && requestedBackOffset < locationStack.size()) {
            int previousBackOffset = backOffset;
            backOffset = requestedBackOffset;
            BrowsingHistoryLocationImpl newLocation = getCurrentLocation();
            checkBeforeUnloadThenCheckBeforeThenTransit(newLocation, BrowsingHistoryEvent.POPPED).setHandler(asyncResult -> {
                if (asyncResult.failed())
                    backOffset = previousBackOffset;
            });
        }
    }

    @Override
    protected void doAcceptedPush(BrowsingHistoryLocationImpl historyLocation) {
        if (backOffset > 0)
            do
                locationStack.pop();
            while (--backOffset != 0);
        locationStack.push(historyLocation);
    }

    protected void doAcceptedReplace(BrowsingHistoryLocationImpl historyLocation) {
        int index = getCurrentLocationIndex();
        if (index != -1)
            locationStack.set(index, historyLocation);
        else
            locationStack.push(historyLocation);
    }

}
