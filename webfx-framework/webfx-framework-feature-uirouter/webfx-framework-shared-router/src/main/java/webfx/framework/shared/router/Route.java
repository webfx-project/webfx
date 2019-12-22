package webfx.framework.shared.router;

import webfx.platform.shared.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface Route {

    Route path(String path);

    Route pathRegex(String path);

    String getPath();

    Route handler(Handler<RoutingContext> handler);

    Route failureHandler(Handler<RoutingContext> exceptionHandler);

    /*
    Route name(String name);

    String name();

    Route parent(Route parent);

    Route parent();

    Route useNormalisedPath(boolean useNormalisedPath);

    Route order(int order);

    Route last();

    Route disable();

    Route enable();

    Route remove();

    Route handler(Handler<RoutingContext> handler);

    */

}
