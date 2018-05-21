package naga.framework.ui.router;

import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class PushRouteRequest extends UiRouteRequest<PushRouteRequest> {

    private JsonObject state;

    public PushRouteRequest(String routePath, History history) {
        this(routePath, history, null);
    }

    public PushRouteRequest(String routePath, History history, JsonObject state) {
        super(routePath, history);
        this.state = state;
    }

    public JsonObject getState() {
        return state;
    }

    public PushRouteRequest setState(JsonObject state) {
        this.state = state;
        return this;
    }

    @Override
    public AsyncFunction<PushRouteRequest, Void> getOperationExecutor() {
        return PushRouteRequest::executePushRouteRequest;
    }

    private static Future<Void> executePushRouteRequest(PushRouteRequest request) {
        String routePath = request.getRoutePath();
        if (routePath != null)
            request.getHistory().push(routePath, request.getState());
        return null;
    }
}
