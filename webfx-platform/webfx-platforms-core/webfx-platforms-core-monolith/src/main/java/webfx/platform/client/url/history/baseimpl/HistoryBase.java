package webfx.platform.client.url.history.baseimpl;

import webfx.platform.services.json.JsonObject;
import webfx.platform.client.url.history.History;
import webfx.platform.client.url.history.HistoryEvent;
import webfx.platform.client.url.history.HistoryLocation;
import webfx.platform.client.url.location.PathStateLocation;
import webfx.platform.client.url.location.impl.PathLocationImpl;
import webfx.platform.client.url.location.impl.PathStateLocationImpl;
import webfx.util.Strings;
import webfx.util.async.Future;
import webfx.util.async.Handler;
import webfx.util.function.Function;

/**
 * @author Bruno Salmon
 */
public abstract class HistoryBase implements History {

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
        checkBeforeUnloadThenCheckBeforeThenTransit(location, HistoryEvent.PUSHED);
    }

    @Override
    public void replace(PathStateLocation location) {
        checkBeforeUnloadThenCheckBeforeThenTransit(location, HistoryEvent.REPLACED);
    }

    protected Future<HistoryLocationImpl> checkBeforeUnloadThenCheckBeforeThenTransit(PathStateLocation location, HistoryEvent event) {
        if (!checkBeforeUnload(getCurrentLocation()))
            return Future.failedFuture("Location refused to unload");
        return checkBeforeThenTransit(location, event);
    }

    protected Future<HistoryLocationImpl> checkBeforeThenTransit(PathStateLocation location, HistoryEvent event) {
        Future<HistoryLocationImpl> future = Future.future();
        HistoryLocationImpl newLocation = location instanceof HistoryLocationImpl ? (HistoryLocationImpl) location : createHistoryLocation(location, event);
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

    protected void transit(HistoryLocationImpl newHistoryLocation) {
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

    protected abstract void doAcceptedPush(HistoryLocationImpl historyLocation);

    protected abstract void doAcceptedReplace(HistoryLocationImpl historyLocation);

    protected void doAcceptedPop(HistoryLocationImpl historyLocation) {} // normally unnecessary


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
    public HistoryLocation createHistoryLocation(String path, JsonObject state) {
        return createHistoryLocation(createPathStateLocation(path, state), null);
    }

    public HistoryLocationImpl createHistoryLocation(PathStateLocation pathStateLocation, HistoryEvent event) {
        return new HistoryLocationImpl(pathStateLocation, event, createLocationKey());
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

    protected boolean checkBeforeUnload(HistoryLocation location) {
        return beforeUnloadTransitionHook == null || !Boolean.FALSE.equals(beforeUnloadTransitionHook.apply(location));
    }

    protected Future<Boolean> checkBeforeAsync(HistoryLocation location) {
        return beforeTransitionHook == null ? Future.succeededFuture(Boolean.TRUE) : beforeTransitionHook.apply(location);
    }

    protected void fireLocationChanged(HistoryLocation location) {
        if (listener != null)
            listener.handle(location);
    }
}
