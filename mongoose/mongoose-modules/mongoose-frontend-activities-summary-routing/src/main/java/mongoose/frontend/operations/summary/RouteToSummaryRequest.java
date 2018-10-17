package mongoose.frontend.operations.summary;

import mongoose.frontend.activities.summary.routing.SummaryRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToSummaryRequest extends RoutePushRequest {

    public RouteToSummaryRequest(Object eventId, BrowsingHistory history) {
        super(SummaryRouting.getSummaryPath(eventId), history);
    }

}
