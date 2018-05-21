package mongoose.activities.backend.organizations;


import naga.framework.operation.HasOperationCode;
import naga.framework.ui.router.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToOrganizationsRequest extends PushRouteRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "ORGANIZATIONS_ROUTING";

    public RouteToOrganizationsRequest(History history) {
        super(OrganizationsRouting.getPath(), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
