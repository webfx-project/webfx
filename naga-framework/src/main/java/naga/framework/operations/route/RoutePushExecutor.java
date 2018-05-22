package naga.framework.operations.route;

import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
class RoutePushExecutor {

    static Future<Void> executePushRouteRequest(RoutePushRequest rq) {
        return execute(rq.getRoutePath(), rq.getHistory(), rq.getState());
    }

    private static Future<Void> execute(String routePath, History history, JsonObject state) {
        if (routePath != null)
            history.push(routePath, state);
        return Future.succeededFuture();
    }
}
