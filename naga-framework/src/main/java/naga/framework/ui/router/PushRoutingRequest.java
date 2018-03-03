package naga.framework.ui.router;

import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public class PushRoutingRequest extends UiRoutingRequest<PushRoutingRequest> {

    private JsonObject state;

    public PushRoutingRequest() {
    }

    public PushRoutingRequest(String routePath, History history) {
        this(routePath, history, null);
    }

    public PushRoutingRequest(String routePath, History history, JsonObject state) {
        super(routePath, history);
        this.state = state;
    }

    public JsonObject getState() {
        return state;
    }

    public PushRoutingRequest setState(JsonObject state) {
        this.state = state;
        return this;
    }

    @Override
    public AsyncFunction<PushRoutingRequest, Void> getOperationExecutor() {
        return request -> {
            String routePath = request.getRoutePath();
            if (routePath != null)
                getHistory().push(routePath, state);
            return null;
        };
    }
}
