package webfx.framework.shared.router.impl;

import webfx.platform.shared.services.json.JsonObject;
import webfx.framework.shared.router.Route;
import webfx.framework.shared.router.Router;
import webfx.framework.shared.router.RoutingContext;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.async.Handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class RouterImpl implements Router {

    private final List<RouteImpl> routes = new ArrayList<>();
    private Handler<Throwable> exceptionHandler;

    @Override
    public Route route() {
        return new RouteImpl(this);
    }

    @Override
    public Route route(String path) {
        return route().path(path);
    }

    @Override
    public Router route(String path, Handler<RoutingContext> handler) {
        route(path).handler(handler);
        return this;
    }

    @Override
    public Route routeWithRegex(String path) {
        return route().pathRegex(path);
    }

    @Override
    public Router routeWithRegex(String path, Handler<RoutingContext> handler) {
        routeWithRegex(path).handler(handler);
        return this;
    }

    void addRoute(RouteImpl route) {
        routes.add(route);
    }

    @Override
    public void accept(String path, JsonObject state) {
        Logger.log("Routing " + path);
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
