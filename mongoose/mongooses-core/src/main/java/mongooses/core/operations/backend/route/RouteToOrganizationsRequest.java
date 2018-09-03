package mongooses.core.operations.backend.route;


import mongooses.core.activities.backend.organizations.OrganizationsRouting;
import webfx.framework.operation.HasOperationCode;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.client.url.history.History;

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
