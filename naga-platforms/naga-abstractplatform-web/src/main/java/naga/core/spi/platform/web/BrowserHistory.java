package naga.core.spi.platform.web;

import naga.core.json.JsonObject;
import naga.core.routing.history.HistoryEvent;
import naga.core.routing.history.impl.HistoryLocationImpl;
import naga.core.routing.history.impl.MemoryHistory;
import naga.core.routing.location.PathStateLocation;
import naga.core.routing.location.WindowLocation;
import naga.core.spi.platform.Platform;
import naga.core.util.Objects;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public class BrowserHistory extends MemoryHistory {

    private final WindowHistory windowHistory;
    private final boolean hashMode = true;
    private String hrefBeforeHash;
    private String lastFragment;

    public BrowserHistory(WindowHistory windowHistory) {
        this.windowHistory = windowHistory;
        //windowHistory.onBeforeUnload(event -> checkBeforeUnload(getCurrentLocation()));
        if (!hashMode)
            windowHistory.onPopState(this::onPopState);
        // Can't access the platform API at this stage since it is currently initializing, so the hashMode will be inited
        // later through checkHashModeInited()
    }

    private void checkHashModeInited() {
        if (hrefBeforeHash == null) {
            WindowLocation wl = getCurrentWindowLocation();
            hrefBeforeHash = Strings.concat(wl.getProtocol(), "://", wl.getHost(), wl.getPathname(), wl.getSearch());
            Platform.get().scheduler().schedulePeriodic(1000, () -> {
                if (!Objects.areEquals(getCurrentWindowLocation().getFragment(), lastFragment))
                    onPopState(null);
            });
        }
    }

    private WindowLocation getCurrentWindowLocation() {
        return ((WebPlatform) Platform.get()).getCurrentLocation();
    }

    private void onPopState(JsonObject state) {
        Platform.log("Entering onPopState");
        // Transforming the current window location into a history location descriptor
        WindowLocation wl = getCurrentWindowLocation();
        String path = hashMode ? lastFragment = wl.getFragment() : wl.getPath();
        Platform.log("path = " + path);
        PathStateLocation pathStateLocation = createPathStateLocation(path, state);
        HistoryLocationImpl location;
        int p = locationStack.indexOf(pathStateLocation);
        Platform.log("Index in stack: " + p);
        if (p == -1)
            super.doAcceptedPush(location = createHistoryLocation(pathStateLocation, HistoryEvent.POPPED));
        else {
            backOffset = p;
            location = locationStack.get(p);
            location.setEvent(HistoryEvent.POPPED);
        }
        // For any reason there is a performance issue with Chrome if we fire the location change now, so we defer it
        Platform.scheduleDeferred(() -> fireLocationChanged(location)); // this will call the router, probably resulting in activity change (with its node)
        Platform.log("Exiting onPopState");
    }

    @Override
    protected void doAcceptedPush(HistoryLocationImpl historyLocation) {
        if (hashMode) {
            checkHashModeInited();
            String href = Strings.concat(hrefBeforeHash, "#", historyLocation.getPathname());
            Platform.log("Assign href = " + href);
            WindowLocation wl = getCurrentWindowLocation();
            wl.assignHref(href);
            lastFragment = wl.getFragment();
        } else
            windowHistory.pushState(historyLocation.getState(), null, createPath(historyLocation));
        super.doAcceptedPush(historyLocation);
    }

    @Override
    protected void doAcceptedReplace(HistoryLocationImpl historyLocation) {
        if (hashMode) {
            checkHashModeInited();
            String href = Strings.concat(hrefBeforeHash, "#", historyLocation.getPathname());
            Platform.log("Replace href = " + href);
            WindowLocation wl = getCurrentWindowLocation();
            wl.replaceHref(href);
            lastFragment = wl.getFragment();
        } else
            windowHistory.replaceState(historyLocation.getState(), null, createPath(historyLocation));
        super.doAcceptedReplace(historyLocation);
    }

    @Override
    public void go(int offset) {
        if (hashMode) {
            Platform.log("Go not yet implemented in hash mode");
        } else
            windowHistory.go(offset);
        super.go(offset);
    }
}
