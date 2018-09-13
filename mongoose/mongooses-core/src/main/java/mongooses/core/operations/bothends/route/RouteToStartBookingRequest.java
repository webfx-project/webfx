package mongooses.core.operations.bothends.route;

import mongooses.core.activities.sharedends.book.start.StartBookingRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.services.browsinghistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToStartBookingRequest extends RoutePushRequest {

    public RouteToStartBookingRequest(Object eventId, BrowsingHistory history) {
        super(StartBookingRouting.getStartBookingPath(eventId), history);
    }

}
