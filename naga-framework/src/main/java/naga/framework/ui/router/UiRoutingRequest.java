package naga.framework.ui.router;

import naga.framework.operation.HasOperationExecutor;
import naga.framework.router.auth.authz.RoutingRequest;
import naga.platform.client.url.history.History;
import naga.platform.json.spi.JsonObject;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class UiRoutingRequest extends RoutingRequest implements HasOperationExecutor<UiRoutingRequest, Void> {

    private History history;
    private JsonObject state;

    public UiRoutingRequest() {
    }

    public UiRoutingRequest(String routePath, History history) {
        this(routePath, history, null);
    }

    public UiRoutingRequest(String routePath, History history, JsonObject state) {
        super(routePath);
        this.history = history;
        this.state = state;
    }

    public History getHistory() {
        return history;
    }

    public UiRoutingRequest setHistory(History history) {
        this.history = history;
        return this;
    }

    public JsonObject getState() {
        return state;
    }

    public UiRoutingRequest setState(JsonObject state) {
        this.state = state;
        return this;
    }

    @Override
    public AsyncFunction<UiRoutingRequest, Void> getOperationExecutor() {
        return request -> {
            String routePath = request.getRoutePath();
            if (routePath != null)
                history.push(routePath, state);
            return null;
        };
    }

    /* Execute the request without checking at this stage if it is authorized or not, because this is actually the
     * router job to do this checking and to redirect to the login or unauthorized page if not authorized */
    public Future<Void> execute() {
        return getOperationExecutor().apply(this);
    }
}
