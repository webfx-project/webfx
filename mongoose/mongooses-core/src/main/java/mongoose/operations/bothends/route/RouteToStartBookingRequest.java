package mongoose.operations.bothends.route;

import mongoose.activities.bothends.book.start.StartBookingRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToStartBookingRequest extends RoutePushRequest {

    public RouteToStartBookingRequest(Object eventId, History history) {
        super(StartBookingRouting.getStartBookingPath(eventId), history);
    }

}
