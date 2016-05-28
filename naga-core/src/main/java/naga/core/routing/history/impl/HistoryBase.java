package naga.core.routing.history.impl;

import naga.core.routing.history.*;
import naga.core.util.Strings;
import naga.core.util.async.Future;
import naga.core.util.function.Function;

/**
 * @author Bruno Salmon
 */
public abstract class HistoryBase implements History {

    @Override
    public void push(String path) {
        push(new LocationDescriptorImpl(path));
    }

    @Override
    public void push(LocationDescriptor location) {
        checkAndTransit(location, HistoryEvent.PUSHED);
    }

    @Override
    public void replace(String path) {
        replace(new LocationDescriptorImpl(path));
    }

    @Override
    public void replace(LocationDescriptor location) {
        checkAndTransit(location, HistoryEvent.REPLACED);
    }

    protected Future<LocationImpl> checkAndTransit(LocationDescriptor location, HistoryEvent event) {
        if (!checkBeforeUnload(getCurrentLocation()))
            return Future.failedFuture("Location refused to unload");
        Future<LocationImpl> future = Future.future();
        LocationImpl newLocation = location instanceof LocationImpl ? (LocationImpl) location : new LocationImpl(location, event, createLocationKey());
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

    protected abstract void doAcceptedPush(LocationImpl location);

    protected abstract void doAcceptedReplace(LocationImpl location);

    protected void doAcceptedPop(LocationImpl location) {} // normally unnecessary


    private int keySeq;
    protected String createLocationKey() {
        return Integer.toString(++keySeq);
    }

    protected abstract void fireLocationChanged(Location location);

    @Override
    public void listenBefore(Function<Location, Boolean> transitionHook) {
        listenBeforeAsync(location -> Future.succeededFuture(transitionHook.apply(location)));
    }

    protected abstract boolean checkBeforeUnload(Location location);

    protected abstract Future<Boolean> checkBeforeAsync(Location location);


    @Override
    public String createHref(LocationDescriptor location) {
        return "#" + createPath(location); // Sure?
    }

    @Override
    public String createPath(LocationDescriptor location) {
        return Strings.concat(location.getPathName(), location.getSearch()); // + Hash?
    }

}
