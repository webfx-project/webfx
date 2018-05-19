package mongoose.activities.shared.book.event.start;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class StartBookingRoutingRequest extends PushRoutingRequest {

    public StartBookingRoutingRequest(Object eventId, History history) {
        super(StartBookingRouting.getStartBookingPath(eventId), history);
    }

}
