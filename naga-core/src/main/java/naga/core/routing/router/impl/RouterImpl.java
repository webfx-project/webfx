package naga.core.routing.router.impl;

import naga.core.json.JsonObject;
import naga.core.routing.router.Route;
import naga.core.routing.router.Router;
import naga.core.routing.router.RoutingContext;
import naga.core.util.async.Handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class RouterImpl implements Router {

    private String currentPath;
    private String defaultPath;
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
    public Router defaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
        return this;
    }

    @Override
    public Router start() {
        if (currentPath == null)
            currentPath = defaultPath;
        if (currentPath == null && !routes.isEmpty())
            currentPath = routes.get(0).getPath();
        accept(currentPath, null);
        return this;
    }

    @Override
    public void accept(String path, JsonObject state) {
        this.currentPath = path;
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
        new RoutingContextWrapper(getAndCheckRoutePath(ctx), ctx.path(), routes, ctx).next();
    }

    @Override
    public void handleFailure(RoutingContext ctx) {
        new RoutingContextWrapper(getAndCheckRoutePath(ctx), ctx.path(), routes, ctx).next();
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
