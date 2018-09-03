package mongooses.core.operations.bothends.route;

import mongooses.core.activities.sharedends.book.person.PersonRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToPersonRequest extends RoutePushRequest {

    public RouteToPersonRequest(Object eventId, History history) {
        super(PersonRouting.getPersonPath(eventId), history);
    }

}
