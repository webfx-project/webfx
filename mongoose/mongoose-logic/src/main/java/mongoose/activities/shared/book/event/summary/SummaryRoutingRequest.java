package mongoose.activities.shared.book.event.summary;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class SummaryRoutingRequest extends PushRoutingRequest {

    public SummaryRoutingRequest(Object eventId, History history) {
        super(SummaryRouting.getSummaryPath(eventId), history);
    }

}
