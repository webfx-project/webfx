package mongooses.core.operations.bothends.route;

import mongooses.core.activities.sharedends.book.summary.SummaryRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToSummaryRequest extends RoutePushRequest {

    public RouteToSummaryRequest(Object eventId, History history) {
        super(SummaryRouting.getSummaryPath(eventId), history);
    }

}
