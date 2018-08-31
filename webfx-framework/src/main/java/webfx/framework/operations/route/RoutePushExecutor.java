package webfx.framework.operations.route;

import webfx.platform.client.url.history.History;
import webfx.platform.services.json.JsonObject;
import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
final class RoutePushExecutor {

    static Future<Void> executePushRouteRequest(RoutePushRequest rq) {
        return execute(rq.getRoutePath(), rq.getHistory(), rq.getState());
    }

    private static Future<Void> execute(String routePath, History history, JsonObject state) {
        if (routePath != null)
            history.push(routePath, state);
        return Future.succeededFuture();
    }
}
