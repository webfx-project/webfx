package mongoose.activities.shared.book.event.summary;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class SummaryRoutingRequest extends PushRoutingRequest {

    public SummaryRoutingRequest(Object eventId, History history) {
        super(MongooseRoutingUtil.interpolateEventIdInPath(eventId, SummaryRouting.PATH), history);
    }
}
