package mongoose.operations.bothends.route;

import mongoose.activities.bothends.book.person.PersonRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToPersonRequest extends RoutePushRequest {

    public RouteToPersonRequest(Object eventId, History history) {
        super(PersonRouting.getPersonPath(eventId), history);
    }

}
