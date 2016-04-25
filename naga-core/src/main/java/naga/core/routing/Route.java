package naga.core.routing;

import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public class Route {

    private final Router router;
    private boolean added;
    private String path;
    private Handler<RoutingContext> handler;

    public Route(Router router) {
        this.router = router;
    }

    Route path(String path) {
        this.path = path;
        return this;
    }

    public String path() {
        return path;
    }

    public Route handler(Handler<RoutingContext> handler) {
        this.handler = handler;
        checkAdded();
        return this;
    }

    boolean matches(RoutingContext context) {
        int i = path.indexOf("/:"); // parameter
        if (i != -1 && context.path().startsWith(path.substring(0, i + 1))) {
            // Capturing parameter (draft implementation assuming only 1 parameter)
            context.getParams().put(path.substring(i + 2), context.path().substring(i + 1));
            return true;
        }
        return removeTrailing(path).equals(removeTrailing(context.path()));
    }

    private static String removeTrailing(String path) {
        int i = path.length();
        return (path.charAt(i - 1) == '/') ?  path.substring(0, i - 1) : path;
    }

    void handleContext(RoutingContext context) {
        if (handler != null)
            handler.handle(context);
    }

    private void checkAdded() {
        if (!added) {
            router.addRoute(this);
            added = true;
        }
    }

    /*
    Route name(String name);

    String name();

    Route parent(Route parent);

    Route parent();


    Route pathRegex(String path);

    Route useNormalisedPath(boolean useNormalisedPath);

    String getPath();

    Route order(int order);

    Route last();

    Route disable();

    Route enable();

    Route remove();

    Route handler(Handler<RoutingContext> handler);

    */


}
