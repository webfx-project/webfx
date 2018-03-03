package naga.framework.ui.router;

import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public final class BackwardRoutingRequest extends UiRoutingRequest<BackwardRoutingRequest> {

    public BackwardRoutingRequest() {
    }

    public BackwardRoutingRequest(History history) {
        super(history);
    }

    @Override
    public AsyncFunction<BackwardRoutingRequest, Void> getOperationExecutor() {
        return request -> {
            request.getHistory().goBack();
            return null;
        };
    }
}
