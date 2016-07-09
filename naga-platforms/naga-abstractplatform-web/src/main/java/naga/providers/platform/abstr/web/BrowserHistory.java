package naga.providers.platform.abstr.web;

import naga.platform.json.spi.JsonObject;
import naga.platform.client.url.history.HistoryEvent;
import naga.platform.client.url.history.baseimpl.HistoryLocationImpl;
import naga.platform.client.url.history.memory.MemoryHistory;
import naga.platform.client.url.location.PathStateLocation;
import naga.platform.client.url.location.WindowLocation;
import naga.platform.spi.Platform;
import naga.commons.util.Objects;
import naga.commons.util.Strings;

/**
 * @author Bruno Salmon
 */
public class BrowserHistory extends MemoryHistory {

    private final WindowHistory windowHistory;
    private final boolean supportsStates;
    private final boolean showHash;

    public BrowserHistory(WindowHistory windowHistory) {
        this.windowHistory = windowHistory;
        supportsStates = windowHistory.supportsStates();
        showHash = true; // !supportsState;
        //windowHistory.onBeforeUnload(event -> checkBeforeUnload(getCurrentLocation()));
        if (supportsStates)
            windowHistory.onPopState(this::onPopState);
        // Can't access the platform API at this stage since it is currently initializing, so the remaining
        // initialization will be done later in checkInitialized()
    }

    private void checkInitialized() {
        if (getMountPoint() == null) {
            WindowLocation wl = getCurrentWindowLocation();
            String mountPath = wl.getPathname();
            if (mountPath.endsWith("/index.html"))
                mountPath = mountPath.substring(0, mountPath.lastIndexOf('/') + 1);
            setMountPoint(mountPath);
            onPopState(supportsStates ? windowHistory.state() : null);
            if (!supportsStates)
                Platform.get().scheduler().schedulePeriodic(500, () -> {
                    if (!Objects.areEquals(getCurrentWindowLocation().getFragment(), getCurrentLocation().getFragment()))
                        onPopState(null);
                });
        }
    }

    @Override
    protected String fullToMountPath(String fullPath) {
        checkInitialized();
        String subPath = super.fullToMountPath(fullPath);
        subPath = Strings.removePrefix(subPath, "index.html");
        subPath = Strings.removePrefix(subPath, "#");
        return subPath;
    }

    @Override
    protected String mountToFullPath(String mountPath) {
        checkInitialized();
        if (showHash && !mountPath.startsWith("#"))
            mountPath = "#" + mountPath;
        return  super.mountToFullPath(mountPath);
    }

    @Override
    public HistoryLocationImpl getCurrentLocation() {
        checkInitialized();
        return super.getCurrentLocation();
    }

    private WindowLocation getCurrentWindowLocation() {
        return ((WebPlatform) Platform.get()).getCurrentLocation();
    }

    private void onPopState(JsonObject state) {
        //Platform.log("Entering onPopState");
        // Transforming the current window location into a history location descriptor
        String path = fullToMountPath(getCurrentWindowLocation().getPath());
        Platform.log("Pop state with path = " + path);
        PathStateLocation pathStateLocation = createPathStateLocation(path, state);
        HistoryLocationImpl location;
        int p = locationStack.indexOf(pathStateLocation);
        //Platform.log("Index in stack: " + p);
        if (p != -1) {
            location = locationStack.get(p);
            location.setEvent(HistoryEvent.POPPED);
            backOffset = p;
        } else
            super.doAcceptedPush(location = createHistoryLocation(pathStateLocation, HistoryEvent.POPPED));
        // For any reason there is a performance issue with Chrome if we fire the location change now, so we defer it
        Platform.scheduleDeferred(() -> fireLocationChanged(location)); // this will call the router, probably resulting in activity change (with its node)
        //Platform.log("Exiting onPopState");
    }

    @Override
    protected void doAcceptedPush(HistoryLocationImpl historyLocation) {
        String path = historyLocation.getPath();
        if (supportsStates)
            windowHistory.pushState(historyLocation.getState(), null, path);
        else
            getCurrentWindowLocation().assignHref(path);
        super.doAcceptedPush(historyLocation);
    }

    @Override
    protected void doAcceptedReplace(HistoryLocationImpl historyLocation) {
        String path = historyLocation.getPath();
        if (supportsStates)
            windowHistory.replaceState(historyLocation.getState(), null, path);
        else
            getCurrentWindowLocation().replaceHref(path);
        super.doAcceptedReplace(historyLocation);
    }

    @Override
    public void go(int offset) {
        windowHistory.go(offset);
        super.go(offset);
    }
}
