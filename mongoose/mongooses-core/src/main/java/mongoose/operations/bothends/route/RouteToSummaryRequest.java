package mongoose.operations.bothends.route;

import mongoose.activities.bothends.book.summary.SummaryRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToSummaryRequest extends RoutePushRequest {

    public RouteToSummaryRequest(Object eventId, History history) {
        super(SummaryRouting.getSummaryPath(eventId), history);
    }

}
