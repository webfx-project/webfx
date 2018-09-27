package mongooses.core.frontend.operations.person;

import mongooses.core.frontend.activities.person.PersonRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToPersonRequest extends RoutePushRequest {

    public RouteToPersonRequest(Object eventId, BrowsingHistory history) {
        super(PersonRouting.getPersonPath(eventId), history);
    }

}
