package webfx.framework.shared.router;

import webfx.platform.shared.services.json.JsonObject;
import webfx.framework.shared.router.impl.RouterImpl;
import webfx.platform.shared.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface Router {

    static Router create() { return new RouterImpl(); }

    Route route();

    Route route(String path);

    Router route(String path, Handler<RoutingContext> handler);

    Route routeWithRegex(String path);

    Router routeWithRegex(String path, Handler<RoutingContext> handler);

    void accept(String path, JsonObject state);

    Router mountSubRouter(String mountPoint, Router subRouter);

    Router exceptionHandler(Handler<Throwable> exceptionHandler);

    void handleContext(RoutingContext ctx);

    void handleFailure(RoutingContext ctx);

}
