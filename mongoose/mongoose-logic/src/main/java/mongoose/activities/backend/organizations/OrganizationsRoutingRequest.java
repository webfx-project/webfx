package mongoose.activities.backend.organizations;


import naga.framework.operation.HasOperationCode;
import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class OrganizationsRoutingRequest extends PushRoutingRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "ORGANIZATIONS_ROUTING";

    public OrganizationsRoutingRequest(History history) {
        super(OrganizationsRouting.PATH, history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}