package webfx.framework.operations.route;

import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;
import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
final class RouteForwardExecutor {

    static Future<Void> executeRequest(RouteForwardRequest rq) {
        return execute(rq.getHistory());
    }

    private static Future<Void> execute(BrowsingHistory history) {
        history.goForward();
        return Future.succeededFuture();
    }
}
