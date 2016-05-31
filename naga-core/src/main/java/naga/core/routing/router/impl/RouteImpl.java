package naga.core.routing.router.impl;

import naga.core.routing.router.Route;
import naga.core.routing.router.RoutingContext;
import naga.core.spi.platform.Platform;
import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
class RouteImpl implements Route {

    private final RouterImpl router;
    private String path;
    private boolean exactPath;
    //private boolean useNormalisedPath;
    private Handler<RoutingContext> contextHandler;
    private Handler<RoutingContext> failureHandler;
    private boolean added;
    private boolean parametrized;
    //private Pattern pattern;
    //private Set<String> groups;

    RouteImpl(RouterImpl router) {
        this.router = router;
    }

    RouterImpl router() {
        return router;
    }

    @Override
    public synchronized Route path(String path) {
        checkPath(path);
        setPath(path);
        return this;
    }

    private void setPath(String path) {
        // See if the path contains ":" - if so then it contains parameter capture groups and we have to generate
        // a regex for that
        parametrized = path.indexOf(':') != -1;
        if (!parametrized) {
            exactPath = path.charAt(path.length() - 1) != '*';
            if (!exactPath)
                path = path.substring(0, path.length() - 1);
        }
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Route handler(Handler<RoutingContext> handler) {
        this.contextHandler = handler;
        checkAdded();
        return this;
    }

    synchronized boolean matches(RoutingContext context, String mountPoint, boolean failure) {
        if (failure && failureHandler == null || !failure && contextHandler == null)
            return false;
        /*if (!enabled) {
            return false;
        }*/
        if (path != null && !parametrized && !pathMatches(mountPoint, context))
            return false;
        if (parametrized) {
            String requestedPath = context.path();
            /*if (useNormalisedPath)
                requestedPath = Utils.normalisePath(useNormalisedPath, false);*/
            if (mountPoint != null)
                requestedPath = requestedPath.substring(mountPoint.length());
            int i = path.indexOf("/:"); // parameter
            if (i != -1 && requestedPath.startsWith(path.substring(0, i + 1))) {
                // Capturing parameter (draft implementation assuming only 1 parameter)
                context.getParams().set(path.substring(i + 2), requestedPath.substring(i + 1));
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean pathMatches(String mountPoint, RoutingContext ctx) {
        String thePath = mountPoint == null ? path : mountPoint + path;
        String requestPath = ctx.path();
        /*if (useNormalisedPath)
            requestPath = Utils.normalisePath(requestPath, false);*/
        if (exactPath)
            return pathMatchesExact(requestPath, thePath);
        else {
            if (thePath.endsWith("/") && requestPath.equals(removeTrailing(thePath)))
                return true;
            return requestPath.startsWith(thePath);
        }
    }

    private boolean pathMatchesExact(String path1, String path2) {
        // Ignore trailing slash when matching paths
        return removeTrailing(path1).equals(removeTrailing(path2));
    }

    private static String removeTrailing(String path) {
        int i = path.length();
        return (path.charAt(i - 1) == '/') ?  path.substring(0, i - 1) : path;
    }

    void handleContext(RoutingContext context) {
        if (contextHandler != null)
            contextHandler.handle(context);
    }

    private void checkAdded() {
        if (!added) {
            router.addRoute(this);
            added = true;
        }
    }

    @Override
    public synchronized Route failureHandler(Handler<RoutingContext> exceptionHandler) {
        if (this.failureHandler != null)
            Platform.log("Setting failureHandler for a route more than once!");
        this.failureHandler = exceptionHandler;
        checkAdded();
        return this;
    }

    private void checkPath(String path) {
        if ("".equals(path) || path.charAt(0) != '/')
            throw new IllegalArgumentException("Path must start with /");
    }

    synchronized void handleFailure(RoutingContext context) {
        if (failureHandler != null)
            failureHandler.handle(context);
    }
}
