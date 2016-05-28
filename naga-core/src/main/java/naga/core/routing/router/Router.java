package naga.core.routing.router;

import naga.core.routing.router.impl.RouterImpl;
import naga.core.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface Router {

    static Router create() { return new RouterImpl(); }

    Route route(String path);

    Router route(String path, Handler<RoutingContext> handler);

    Router defaultPath(String defaultPath);

    Router start();

    void accept(String path);

    Router mountSubRouter(String mountPoint, Router subRouter);

    Router exceptionHandler(Handler<Throwable> exceptionHandler);

    void handleContext(RoutingContext ctx);

    void handleFailure(RoutingContext ctx);

    /*
    Router goTo(Route route);

    Router goTo(String routeName);

    Router go(int delta);

    default Router back() { return go(-1); }

    default Router forward() { return go(1); }

    int historyLength();
    */

}
