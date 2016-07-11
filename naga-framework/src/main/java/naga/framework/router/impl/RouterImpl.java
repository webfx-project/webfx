package naga.framework.router.impl;

import naga.platform.json.spi.JsonObject;
import naga.framework.router.Route;
import naga.framework.router.Router;
import naga.framework.router.RoutingContext;
import naga.commons.util.async.Handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class RouterImpl implements Router {

    private List<RouteImpl> routes = new ArrayList<>();
    private Handler<Throwable> exceptionHandler;

    @Override
    public Route route(String path) {
        return new RouteImpl(this).path(path);
    }

    @Override
    public Router route(String path, Handler<RoutingContext> handler) {
        new RouteImpl(this).path(path).handler(handler);
        return this;
    }

    void addRoute(RouteImpl route) {
        routes.add(route);
    }

    @Override
    public void accept(String path, JsonObject state) {
        new RoutingContextImpl(null, this, path, routes, state).next();
    }

    @Override
    public synchronized Router exceptionHandler(Handler<Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    Handler<Throwable> exceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public Router mountSubRouter(String mountPoint, Router subRouter) {
        if (mountPoint.endsWith("*"))
            throw new IllegalArgumentException("Don't include * when mounting subrouter");
        if (mountPoint.contains(":"))
            throw new IllegalArgumentException("Can't use patterns in subrouter mounts");
        route(mountPoint + "*").handler(subRouter::handleContext).failureHandler(subRouter::handleFailure);
        return this;
    }

    @Override
    public void handleContext(RoutingContext ctx) {
        new SubRoutingContext(getAndCheckRoutePath(ctx), ctx.path(), routes, ctx).next();
    }

    @Override
    public void handleFailure(RoutingContext ctx) {
        new SubRoutingContext(getAndCheckRoutePath(ctx), ctx.path(), routes, ctx).next();
    }

    private String getAndCheckRoutePath(RoutingContext ctx) {
        Route currentRoute = ctx.currentRoute();
        String path = currentRoute.getPath();
        if (path == null)
            throw new IllegalStateException("Sub routers must be mounted on constant paths (no regex or patterns)");
        return path;
    }

    Iterator<RouteImpl> iterator() {
        return routes.iterator();
    }
}
