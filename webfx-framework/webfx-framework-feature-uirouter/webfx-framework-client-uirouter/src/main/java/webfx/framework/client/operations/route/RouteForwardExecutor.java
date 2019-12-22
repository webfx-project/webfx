package webfx.framework.client.operations.route;

import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;
import webfx.platform.shared.util.async.Future;

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
