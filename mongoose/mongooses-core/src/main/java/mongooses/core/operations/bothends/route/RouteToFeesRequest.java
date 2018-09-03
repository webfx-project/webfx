package mongooses.core.operations.bothends.route;

import mongooses.core.activities.sharedends.book.fees.FeesRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToFeesRequest extends RoutePushRequest {

    public RouteToFeesRequest(Object eventId, History history) {
        super(FeesRouting.getFeesPath(eventId), history);
    }

}
