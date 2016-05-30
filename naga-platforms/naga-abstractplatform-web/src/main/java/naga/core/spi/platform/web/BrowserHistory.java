package naga.core.spi.platform.web;

import naga.core.routing.history.impl.HistoryLocationImpl;
import naga.core.routing.history.impl.MemoryHistory;

/**
 * @author Bruno Salmon
 */
public class BrowserHistory extends MemoryHistory {

    private final WindowHistory windowHistory;

    public BrowserHistory(WindowHistory windowHistory) {
        this.windowHistory = windowHistory;
        //windowHistory.onBeforeUnload(event -> checkBeforeUnload(getCurrentLocation()));
        //windowHistory.onPopState(state -> fireLocationChanged(getCurrentLocation()));
    }

    @Override
    protected void doAcceptedPush(HistoryLocationImpl location) {
        super.doAcceptedPush(location);
        windowHistory.pushState(location.getState(), null, createPath(location));
    }

    @Override
    protected void doAcceptedReplace(HistoryLocationImpl location) {
        super.doAcceptedReplace(location);
        windowHistory.replaceState(location.getState(), null, createPath(location));
    }

    /*@Override
    public HistoryLocationImpl getCurrentLocation() {
        WindowLocation loc = ((WebPlatform) Platform.get()).getCurrentLocation();
        return new HistoryLocationImpl(loc.getPathName(), loc.getSearch(),  windowHistory.state(), HistoryEvent.POPPED, null);
    }*/

    @Override
    public void go(int offset) {
        super.go(offset);
        windowHistory.go(offset);
    }
}
