package mongoose.operations.backend.route;


import mongoose.activities.backend.operations.OperationsRouting;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToOperationsRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToOperations";

    public RouteToOperationsRequest(History history) {
        super(OperationsRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
