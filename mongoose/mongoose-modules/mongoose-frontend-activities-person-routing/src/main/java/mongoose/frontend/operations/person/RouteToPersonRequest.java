package mongoose.frontend.operations.person;

import mongoose.frontend.activities.person.routing.PersonRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToPersonRequest extends RoutePushRequest {

    public RouteToPersonRequest(Object eventId, BrowsingHistory history) {
        super(PersonRouting.getPersonPath(eventId), history);
    }

}
