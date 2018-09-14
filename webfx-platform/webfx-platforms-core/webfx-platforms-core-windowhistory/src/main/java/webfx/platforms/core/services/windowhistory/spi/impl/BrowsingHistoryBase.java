package webfx.platforms.core.services.windowhistory.spi.impl;

import webfx.platforms.core.services.windowhistory.spi.BrowsingHistoryEvent;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistoryLocation;
import webfx.platforms.core.services.windowlocation.spi.PathStateLocation;
import webfx.platforms.core.services.windowlocation.spi.impl.PathLocationImpl;
import webfx.platforms.core.services.windowlocation.spi.impl.PathStateLocationImpl;
import webfx.platforms.core.util.Strings;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.async.Handler;
import webfx.platforms.core.util.function.Function;

/**
 * @author Bruno Salmon
 */
public abstract class BrowsingHistoryBase implements BrowsingHistory {

    private String mountPoint;

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    protected String getMountPoint() {
        return mountPoint;
    }

    protected String mountToFullPath(String mountPath) {
        if ("/".equals(mountPoint))
            return mountPath;
        return Strings.concat(mountPoint, mountPath);
    }

    protected String fullToMountPath(String fullPath) {
        return Strings.removePrefix(fullPath, mountPoint);
    }

    @Override
    public void push(PathStateLocation location) {
        checkBeforeUnloadThenCheckBeforeThenTransit(location, BrowsingHistoryEvent.PUSHED);
    }

    @Override
    public void replace(PathStateLocation location) {
        checkBeforeUnloadThenCheckBeforeThenTransit(location, BrowsingHistoryEvent.REPLACED);
    }

    protected Future<BrowsingHistoryLocationImpl> checkBeforeUnloadThenCheckBeforeThenTransit(PathStateLocation location, BrowsingHistoryEvent event) {
        if (!checkBeforeUnload(getCurrentLocation()))
            return Future.failedFuture("Location refused to unload");
        return checkBeforeThenTransit(location, event);
    }

    protected Future<BrowsingHistoryLocationImpl> checkBeforeThenTransit(PathStateLocation location, BrowsingHistoryEvent event) {
        Future<BrowsingHistoryLocationImpl> future = Future.future();
        BrowsingHistoryLocationImpl newLocation = location instanceof BrowsingHistoryLocationImpl ? (BrowsingHistoryLocationImpl) location : createHistoryLocation(location, event);
        newLocation.setEvent(event);
        checkBeforeAsync(newLocation).setHandler(asyncResult -> {
            if (asyncResult.failed() || !asyncResult.result())
                future.fail("checkBefore refused transition");
            else {
                transit(newLocation);
                future.complete(newLocation);
            }
        } );
        return future;
    }

    protected void transit(BrowsingHistoryLocationImpl newHistoryLocation) {
        switch (newHistoryLocation.getEvent()) {
            case PUSHED:
                doAcceptedPush(newHistoryLocation);
                break;
            case REPLACED:
                doAcceptedReplace(newHistoryLocation);
                break;
            case POPPED:
                doAcceptedPop(newHistoryLocation);
                break;
        }
        fireLocationChanged(newHistoryLocation);
    }

    protected abstract void doAcceptedPush(BrowsingHistoryLocationImpl historyLocation);

    protected abstract void doAcceptedReplace(BrowsingHistoryLocationImpl historyLocation);

    protected void doAcceptedPop(BrowsingHistoryLocationImpl historyLocation) {} // normally unnecessary


    private int keySeq;
    protected String createLocationKey() {
        return Integer.toString(++keySeq);
    }

    @Override
    public String getPath(PathStateLocation location) {
        return fullToMountPath(location.getPath());
    }

    @Override
    public PathStateLocation createPathStateLocation(String path, JsonObject state) {
        path = mountToFullPath(path);
        return new PathStateLocationImpl(new PathLocationImpl(path), state);
    }

    @Override
    public BrowsingHistoryLocation createHistoryLocation(String path, JsonObject state) {
        return createHistoryLocation(createPathStateLocation(path, state), null);
    }

    public BrowsingHistoryLocationImpl createHistoryLocation(PathStateLocation pathStateLocation, BrowsingHistoryEvent event) {
        return new BrowsingHistoryLocationImpl(pathStateLocation, event, createLocationKey());
    }


    /**************************************** Transitions management ***************************************************
     *
     *  A transition is the process of notifying listeners when the location changes.
     *  It is not an API; rather, it is a concept. Transitions may be interrupted by transition hooks
     *  Note: A transition does not refer to the exact moment the URL actually changes. For example, in web browsers
     *  the user may click the back button or otherwise directly manipulate the URL by typing into the address bar.
     *  This is not a transition, but a history object will start a transition as a result of the URL changing.
     *
     ******************************************************************************************************************/

    private Handler<BrowsingHistoryLocation> listener;
    @Override
    public void listen(Handler<BrowsingHistoryLocation> listener) {
        this.listener = listener;
    }

    private Function<BrowsingHistoryLocation, Future<Boolean>> beforeTransitionHook;
    @Override
    public void listenBeforeAsync(Function<BrowsingHistoryLocation, Future<Boolean>> transitionHook) {
        beforeTransitionHook = transitionHook;
    }

    private Function<BrowsingHistoryLocation, Boolean> beforeUnloadTransitionHook;
    @Override
    public void listenBeforeUnload(Function<BrowsingHistoryLocation, Boolean> transitionHook) {
        beforeUnloadTransitionHook = transitionHook;
    }

    protected boolean checkBeforeUnload(BrowsingHistoryLocation location) {
        return beforeUnloadTransitionHook == null || !Boolean.FALSE.equals(beforeUnloadTransitionHook.apply(location));
    }

    protected Future<Boolean> checkBeforeAsync(BrowsingHistoryLocation location) {
        return beforeTransitionHook == null ? Future.succeededFuture(Boolean.TRUE) : beforeTransitionHook.apply(location);
    }

    protected void fireLocationChanged(BrowsingHistoryLocation location) {
        if (listener != null)
            listener.handle(location);
    }
}
