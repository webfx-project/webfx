package mongoose.frontend.operations.startbooking;

import mongoose.frontend.activities.startbooking.routing.StartBookingRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToStartBookingRequest extends RoutePushRequest {

    public RouteToStartBookingRequest(Object eventId, BrowsingHistory history) {
        super(StartBookingRouting.getStartBookingPath(eventId), history);
    }

}
