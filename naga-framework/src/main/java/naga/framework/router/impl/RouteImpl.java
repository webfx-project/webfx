package naga.framework.router.impl;

import naga.framework.router.Route;
import naga.framework.router.RoutingContext;
import naga.platform.services.log.spi.Logger;
import naga.util.Strings;
import naga.util.async.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private boolean parametrized; // used to flag non regex paths with parameters
    private Pattern pattern;
    private List<String> groups;

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

    private void checkPath(String path) {
        if (!Strings.startsWith(path, "/"))
            throw new IllegalArgumentException("Path must start with /");
    }

    private void setPath(String path) {
        // See if the path contains ":" - if so then it contains parameter capture groups and we have to generate
        // a regex for that
        parametrized = path.indexOf(':') != -1;
        if (!parametrized) {
            int lastCharPos = path.length() - 1;
            exactPath = path.charAt(lastCharPos) != '*';
            if (!exactPath)
                path = path.substring(0, lastCharPos);
        }
        this.path = path;
    }

    @Override
    public synchronized Route pathRegex(String regex) {
        setRegex(regex);
        return this;
    }

    private void setRegex(String regex) {
        pattern = Pattern.compile(regex);
        // Check if there are any groups with names
        for (int i = regex.indexOf('('); i >= 0 ; i = regex.indexOf('(', i + 1)) {
            if (i == 0 || regex.charAt(i - 1) != '\\') {
                if (groups == null)
                    groups = new ArrayList<>();
                if (i >= regex.length() - 2 || regex.charAt(i + 1) != '?' || regex.charAt(i + 2) != '<')
                    groups.add(null);
                else
                    groups.add(regex.substring(i + 3, regex.indexOf('>', i + 3)));
            }
        }
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
        if (path != null && !parametrized && pattern == null && !pathMatches(mountPoint, context))
            return false;
        String requestedPath = context.path(); //useNormalisedPath ? Utils.normalizePath(context.request().path()) : context.request().path();
        if (Strings.isNotEmpty(mountPoint))
            requestedPath = requestedPath.substring(mountPoint.length());
        if (parametrized) {
            int pathPos = 0, reqPos = 0, pathLength = path.length(), reqLength = requestedPath.length();
            while (true) {
                if (pathPos == pathLength && reqPos == reqLength) // Means that the loop is successfully finished
                    return true;
                if (pathPos >= pathLength || reqPos >= reqLength) // Means that the paths don't have the same number of tokens
                    return false;
                // Now comparing the next token
                if (path.charAt(pathPos) != '/' || requestedPath.charAt(reqPos) != '/') // it should start with / on both paths
                    return false;
                // Searching the end of token position
                int nextPathPos = path.indexOf('/', pathPos + 1);
                if (nextPathPos == -1)
                    nextPathPos = pathLength;
                int nextReqPos = requestedPath.indexOf('/', reqPos + 1);
                if (nextReqPos == -1)
                    nextReqPos = reqLength;
                // Capturing the token in the requested path
                String reqToken = requestedPath.substring(reqPos + 1, nextReqPos);
                // And comparing it with the token in the route path (2 cases: parameter or literal token)
                if (path.charAt(pathPos + 1) == ':') // If the route path token is a parameter
                    // We record the parameter value in the context
                    context.getParams().set(/* parameter name: */ path.substring(pathPos + 2, nextPathPos), /* parameter value: */ reqToken);
                else // Otherwise (if the route path token is a literal string), we just check that both tokens are equals
                    if (!path.substring(pathPos + 1, nextPathPos).equals(reqToken))
                        return false;
                pathPos = nextPathPos;
                reqPos = nextReqPos;
            }
        } else if (pattern != null) {
            Matcher m = pattern.matcher(requestedPath);
            if (!m.matches())
                return false;
            // capturing groups
            int n = m.groupCount();
            if (n > 0 && groups != null) {
                int gn = groups.size();
                for (int i = 0; i < n; i++) {
                    String group = m.group(i + 1);
                    if (group != null) {
                        String k = i < gn ? groups.get(i) : null;
                        if (k != null) {
                            String value = group; // Utils.urlDecode(group, false);
                            context.getParams().set(k, value);
                        }
                    }
                }
            }
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
        int lastCharPos = path.length() - 1;
        return (path.charAt(lastCharPos) == '/') ? path.substring(0, lastCharPos) : path;
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
            Logger.log("Setting failureHandler for a route more than once!");
        this.failureHandler = exceptionHandler;
        checkAdded();
        return this;
    }

    synchronized void handleFailure(RoutingContext context) {
        if (failureHandler != null)
            failureHandler.handle(context);
    }
}
