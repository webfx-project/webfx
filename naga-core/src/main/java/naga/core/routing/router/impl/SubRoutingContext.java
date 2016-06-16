package naga.core.routing.router.impl;

import naga.core.json.WritableJsonObject;
import naga.core.routing.router.RoutingContext;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
class SubRoutingContext extends RoutingContextImplBase {

    protected final RoutingContext inner;
    private final String mountPoint;

    SubRoutingContext(String mountPoint, String path, Collection<RouteImpl> routes, RoutingContext inner) {
        super(mountPoint, path, routes, null);
        this.inner = inner;
        String parentMountPoint = inner.mountPoint();
        // Removing the trailing slash or we won't match
        if (mountPoint.charAt(mountPoint.length() - 1) == '/')
            mountPoint = mountPoint.substring(0, mountPoint.length() - 1);
        this.mountPoint = parentMountPoint == null ? mountPoint : parentMountPoint + mountPoint;
    }

    @Override
    public String mountPoint() {
        return mountPoint;
    }

    @Override
    public String path() {
        return inner.path();
    }

    @Override
    public WritableJsonObject getParams() {
        return inner.getParams();
    }

    @Override
    public void next() {
        if (!super.iterateNext()) {
            // We didn't route request to anything so go to parent
            inner.next();
        }
    }

    @Override
    public void fail(int statusCode) {
        inner.fail(statusCode);
    }

    @Override
    public void fail(Throwable throwable) {
        inner.fail(throwable);
    }

    @Override
    public int statusCode() {
        return inner.statusCode();
    }

    @Override
    public boolean failed() {
        return inner.failed();
    }

    @Override
    public Throwable failure() {
        return inner.failure();
    }

}
