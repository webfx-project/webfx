package naga.framework.ui.router;

import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public final class ForwardRoutingRequest extends UiRoutingRequest<ForwardRoutingRequest> {

    public ForwardRoutingRequest() {
    }

    public ForwardRoutingRequest(History history) {
        super(history);
    }

    @Override
    public AsyncFunction<ForwardRoutingRequest, Void> getOperationExecutor() {
        return request -> {
            request.getHistory().goForward();
            return null;
        };
    }
}
