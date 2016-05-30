package naga.core.routing.history.impl;

import naga.core.json.JsonObject;
import naga.core.routing.history.*;
import naga.core.util.Strings;
import naga.core.util.async.Future;
import naga.core.util.function.Function;

/**
 * @author Bruno Salmon
 */
public abstract class HistoryBase implements History {

    @Override
    public void push(HistoryLocationDescriptor location) {
        checkAndTransit(location, HistoryEvent.PUSHED);
    }

    @Override
    public void replace(HistoryLocationDescriptor location) {
        checkAndTransit(location, HistoryEvent.REPLACED);
    }

    protected Future<HistoryLocationImpl> checkAndTransit(HistoryLocationDescriptor location, HistoryEvent event) {
        if (!checkBeforeUnload(getCurrentLocation()))
            return Future.failedFuture("Location refused to unload");
        Future<HistoryLocationImpl> future = Future.future();
        HistoryLocationImpl newLocation = location instanceof HistoryLocationImpl ? (HistoryLocationImpl) location : createLocation(location, event);
        newLocation.setEvent(event);
        checkBeforeAsync(newLocation).setHandler(asyncResult -> {
            if (asyncResult.succeeded() && asyncResult.result())
                switch (event) {
                    case PUSHED:    doAcceptedPush(newLocation);    break;
                    case REPLACED:  doAcceptedReplace(newLocation); break;
                    case POPPED:    doAcceptedPop(newLocation);     break;
                }
            future.complete(newLocation);
            fireLocationChanged(newLocation);
        } );
        return future;
    }

    protected abstract void doAcceptedPush(HistoryLocationImpl location);

    protected abstract void doAcceptedReplace(HistoryLocationImpl location);

    protected void doAcceptedPop(HistoryLocationImpl location) {} // normally unnecessary


    private int keySeq;
    protected String createLocationKey() {
        return Integer.toString(++keySeq);
    }

    protected abstract void fireLocationChanged(HistoryLocation location);

    @Override
    public void listenBefore(Function<HistoryLocation, Boolean> transitionHook) {
        listenBeforeAsync(location -> Future.succeededFuture(transitionHook.apply(location)));
    }

    protected abstract boolean checkBeforeUnload(HistoryLocation location);

    protected abstract Future<Boolean> checkBeforeAsync(HistoryLocation location);

    @Override
    public String createHref(HistoryLocationDescriptor location) {
        return createPath(location); // It seems a HashHistory will override this method to prepend it with '#'
    }

    @Override
    public String createPath(HistoryLocationDescriptor location) {
        return Strings.concat(location.getPathName(), location.getSearch(), location.getHash());
    }

    @Override
    public HistoryLocationDescriptor createLocationDescriptor(String path, JsonObject state) {
        String pathname = path;
        String search = null;
        String hash = null;
        int p = pathname.indexOf('#');
        if (p != -1) {
            hash = pathname.substring(p);
            pathname = pathname.substring(0, p);
        }
        p = pathname.indexOf('?');
        if (p != -1) {
            search = pathname.substring(p);
            pathname = pathname.substring(0, p);
        }
        return new HistoryLocationDescriptorImpl(pathname, search, hash, state);
    }

    @Override
    public HistoryLocation createLocation(String path, JsonObject state) {
        return createLocation(createLocationDescriptor(path, state), null);
    }

    public HistoryLocationImpl createLocation(HistoryLocationDescriptor locationDescriptor, HistoryEvent event) {
        return new HistoryLocationImpl(locationDescriptor, event, createLocationKey());
    }
}
