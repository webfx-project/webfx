package mongoose.activities.backend.organizations;


import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class OrganizationsRoutingRequest extends UiRoutingRequest {

    public OrganizationsRoutingRequest(History history) {
        super(OrganizationsRouting.PATH, history);
    }
}
