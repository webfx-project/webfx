package naga.core.routing.router;

import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface Route {

    Route path(String path);

    String getPath();

    Route handler(Handler<RoutingContext> handler);

    Route failureHandler(Handler<RoutingContext> exceptionHandler);

    /*
    Route name(String name);

    String name();

    Route parent(Route parent);

    Route parent();

    Route pathRegex(String path);

    Route useNormalisedPath(boolean useNormalisedPath);

    Route order(int order);

    Route last();

    Route disable();

    Route enable();

    Route remove();

    Route handler(Handler<RoutingContext> handler);

    */

}
