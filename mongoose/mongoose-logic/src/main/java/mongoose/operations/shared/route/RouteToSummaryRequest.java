package mongoose.operations.shared.route;

import mongoose.activities.shared.book.summary.SummaryRouting;
import naga.framework.operations.route.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToSummaryRequest extends PushRouteRequest {

    public RouteToSummaryRequest(Object eventId, History history) {
        super(SummaryRouting.getSummaryPath(eventId), history);
    }

}
