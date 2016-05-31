package naga.core.spi.platform.web;

import naga.core.json.JsonObject;
import naga.core.routing.history.HistoryEvent;
import naga.core.routing.history.HistoryLocationDescriptor;
import naga.core.routing.history.impl.HistoryLocationImpl;
import naga.core.routing.history.impl.MemoryHistory;
import naga.core.spi.platform.Platform;
import naga.core.util.Strings;

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
        HistoryLocationDescriptor locationDescriptor = createLocationDescriptor(Strings.concat(loc.getPathName(), loc.getSearch(), loc.getHash()), state);
        HistoryLocationImpl location;
        int p = locationStack.indexOf(locationDescriptor);
        //Platform.log("Index in stack: " + p);
        if (p == -1)
            super.doAcceptedPush(location = createLocation(locationDescriptor, HistoryEvent.POPPED));
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
    protected void doAcceptedPush(HistoryLocationImpl location) {
        windowHistory.pushState(location.getState(), null, createPath(location));
        super.doAcceptedPush(location);
    }

    @Override
    protected void doAcceptedReplace(HistoryLocationImpl location) {
        windowHistory.replaceState(location.getState(), null, createPath(location));
        super.doAcceptedReplace(location);
    }

    @Override
    public void go(int offset) {
        windowHistory.go(offset);
        super.go(offset);
    }
}
