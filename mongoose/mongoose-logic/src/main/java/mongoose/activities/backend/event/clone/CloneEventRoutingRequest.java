package mongoose.activities.backend.event.clone;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class CloneEventRoutingRequest extends UiRoutingRequest {

    public CloneEventRoutingRequest(Object eventId, History history) {
        super(MongooseRoutingUtil.interpolateEventIdInPath(eventId, CloneEventRouting.PATH), history);
    }
}
