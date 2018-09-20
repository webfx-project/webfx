package webfx.framework.operations.route;

import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class RoutePushRequest extends RouteRequestBase<RoutePushRequest> {

    private JsonObject state;

    public RoutePushRequest(String routePath, BrowsingHistory browsingHistory) {
        this(routePath, browsingHistory, null);
    }

    public RoutePushRequest(String routePath, BrowsingHistory history, JsonObject state) {
        super(routePath, history);
        this.state = state;
    }

    public JsonObject getState() {
        return state;
    }

    public RoutePushRequest setState(JsonObject state) {
        this.state = state;
        return this;
    }

    @Override
    public AsyncFunction<RoutePushRequest, Void> getOperationExecutor() {
        return RoutePushExecutor::executePushRouteRequest;
    }
}
