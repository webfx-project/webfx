package mongoose.operations.shared.route;

import mongoose.activities.shared.book.start.StartBookingRouting;
import naga.framework.operations.route.RoutePushRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToStartBookingRequest extends RoutePushRequest {

    public RouteToStartBookingRequest(Object eventId, History history) {
        super(StartBookingRouting.getStartBookingPath(eventId), history);
    }

}
