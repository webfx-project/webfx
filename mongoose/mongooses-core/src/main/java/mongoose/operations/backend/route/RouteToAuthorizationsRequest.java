package mongoose.operations.backend.route;


import mongoose.activities.backend.authorizations.AuthorizationsRouting;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToAuthorizationsRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToAuthorizations";

    public RouteToAuthorizationsRequest(History history) {
        super(AuthorizationsRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
