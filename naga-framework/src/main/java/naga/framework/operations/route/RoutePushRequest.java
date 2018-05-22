package naga.framework.operations.route;

import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class RoutePushRequest extends RouteHistoryRequest<RoutePushRequest> {

    private JsonObject state;

    public RoutePushRequest(String routePath, History history) {
        this(routePath, history, null);
    }

    public RoutePushRequest(String routePath, History history, JsonObject state) {
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
