package mongoose.activities.backend.events;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class EventsRoutingRequest extends PushRoutingRequest {

    public EventsRoutingRequest(History history) {
        super(EventsRouting.ALL_EVENTS_PATH, history);
    }

    public EventsRoutingRequest(Object organizationId, History history) {
        super(MongooseRoutingUtil.interpolateOrganizationIdInPath(organizationId, EventsRouting.ORGANIZATION_PATH), history);
    }

}
