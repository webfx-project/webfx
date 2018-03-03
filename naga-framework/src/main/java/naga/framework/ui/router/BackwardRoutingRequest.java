package naga.framework.ui.router;

import naga.framework.operation.HasOperationCode;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;

/**
 * @author Bruno Salmon
 */
public final class BackwardRoutingRequest
        extends UiRoutingRequest<BackwardRoutingRequest>
        implements HasOperationCode {

    private static final String OPERATION_CODE = "BACKWARD";

    public BackwardRoutingRequest() {
    }

    public BackwardRoutingRequest(History history) {
        super(history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<BackwardRoutingRequest, Void> getOperationExecutor() {
        return request -> {
            request.getHistory().goBack();
            return null;
        };
    }
}
