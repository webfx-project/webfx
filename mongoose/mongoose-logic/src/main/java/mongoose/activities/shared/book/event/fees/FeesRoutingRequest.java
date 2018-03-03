package mongoose.activities.shared.book.event.fees;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class FeesRoutingRequest extends PushRoutingRequest {

    public FeesRoutingRequest(Object eventId, History history) {
        super(MongooseRoutingUtil.interpolateEventIdInPath(eventId, FeesRouting.PATH), history);
    }
}
