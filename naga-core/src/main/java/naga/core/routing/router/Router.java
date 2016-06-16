package naga.core.routing.router;

import naga.core.json.JsonObject;
import naga.core.routing.router.impl.RouterImpl;
import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface Router {

    static Router create() { return new RouterImpl(); }

    Route route(String path);

    Router route(String path, Handler<RoutingContext> handler);

    void accept(String path, JsonObject state);

    Router mountSubRouter(String mountPoint, Router subRouter);

    Router exceptionHandler(Handler<Throwable> exceptionHandler);

    void handleContext(RoutingContext ctx);

    void handleFailure(RoutingContext ctx);

}
