package mongoose.operations.shared.route;

import mongoose.activities.shared.book.person.PersonRouting;
import naga.framework.operations.route.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToPersonRequest extends PushRouteRequest {

    public RouteToPersonRequest(Object eventId, History history) {
        super(PersonRouting.getPersonPath(eventId), history);
    }

}
