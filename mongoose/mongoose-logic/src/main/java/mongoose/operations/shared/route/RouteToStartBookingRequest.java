package mongoose.operations.shared.route;

import mongoose.activities.shared.book.start.StartBookingRouting;
import naga.framework.operations.route.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToStartBookingRequest extends PushRouteRequest {

    public RouteToStartBookingRequest(Object eventId, History history) {
        super(StartBookingRouting.getStartBookingPath(eventId), history);
    }

}
