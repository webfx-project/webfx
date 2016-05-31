package naga.core.spi.platform.web;

import naga.core.json.JsonObject;
import naga.core.routing.history.HistoryEvent;
import naga.core.routing.history.impl.HistoryLocationImpl;
import naga.core.routing.history.impl.MemoryHistory;
import naga.core.routing.location.PathStateLocation;
import naga.core.routing.location.WindowLocation;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class BrowserHistory extends MemoryHistory {

    private final WindowHistory windowHistory;

    public BrowserHistory(WindowHistory windowHistory) {
        this.windowHistory = windowHistory;
        //windowHistory.onBeforeUnload(event -> checkBeforeUnload(getCurrentLocation()));
        windowHistory.onPopState(this::onPopState);
    }

    private void onPopState(JsonObject state) {
        //Platform.log("Entering onPopState");
        // Transforming the current window location into a history location descriptor
        WindowLocation loc = ((WebPlatform) Platform.get()).getCurrentLocation();
        PathStateLocation pathStateLocation = createPathStateLocation(loc.getPath(), state);
        HistoryLocationImpl location;
        int p = locationStack.indexOf(pathStateLocation);
        //Platform.log("Index in stack: " + p);
        if (p == -1)
            super.doAcceptedPush(location = createHistoryLocation(pathStateLocation, HistoryEvent.POPPED));
        else {
            backOffset = p;
            location = locationStack.get(p);
            location.setEvent(HistoryEvent.POPPED);
        }
        // For any reason there is a performance issue with Chrome if we fire the location change now, so we defer it
        Platform.scheduleDeferred(() -> fireLocationChanged(location)); // this will call the router, probably resulting in activity change (with its node)
        //Platform.log("Exiting onPopState");
    }

    @Override
    protected void doAcceptedPush(HistoryLocationImpl historyLocation) {
        windowHistory.pushState(historyLocation.getState(), null, createPath(historyLocation));
        super.doAcceptedPush(historyLocation);
    }

    @Override
    protected void doAcceptedReplace(HistoryLocationImpl historyLocation) {
        windowHistory.replaceState(historyLocation.getState(), null, createPath(historyLocation));
        super.doAcceptedReplace(historyLocation);
    }

    @Override
    public void go(int offset) {
        windowHistory.go(offset);
        super.go(offset);
    }
}
