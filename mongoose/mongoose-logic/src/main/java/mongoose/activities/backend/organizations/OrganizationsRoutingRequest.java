package mongoose.activities.backend.organizations;


import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class OrganizationsRoutingRequest extends PushRoutingRequest {

    public OrganizationsRoutingRequest(History history) {
        super(OrganizationsRouting.PATH, history);
    }
}
