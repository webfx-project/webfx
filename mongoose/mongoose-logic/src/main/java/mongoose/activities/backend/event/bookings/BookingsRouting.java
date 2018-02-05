package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.router.util.PathBuilder;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class BookingsRouting {

    private final static String ANY_PATH = "/bookings(/organization/:organizationId|/event/:eventId|/day/:day)*";
    private final static String EVENT_PATH = "/bookings/event/:eventId";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PathBuilder.toRegexPath(ANY_PATH),true,
                true, BookingsPresentationActivity::new, DomainPresentationActivityContextFinal::new, null
        );
    }

    public static void routeUsingEvent(Entity event, History history) {
        MongooseRoutingUtil.routeUsingEntityPrimaryKey(event, history, BookingsRouting::routeUsingEventId);
    }

    public static void routeUsingEventId(Object eventId, History history) {
        history.push(MongooseRoutingUtil.interpolateEventIdInPath(eventId, EVENT_PATH));
    }

}
