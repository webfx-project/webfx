package mongoose.operations.backend.route;


import mongoose.activities.backend.organizations.OrganizationsRouting;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToOrganizationsRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToOrganizations";

    public RouteToOrganizationsRequest(History history) {
        super(OrganizationsRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
