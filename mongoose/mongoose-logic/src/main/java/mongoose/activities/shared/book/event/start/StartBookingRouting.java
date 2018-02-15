package mongoose.activities.shared.book.event.start;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class StartBookingRouting {

    private final static String PATH = "/book/event/:eventId/start";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , StartBookingViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static void routeUsingEvent(Entity event, History history) {
        MongooseRoutingUtil.routeUsingEntityId(event, history, StartBookingRouting::routeUsingEventId);
    }

    public static void routeUsingEventId(Object eventId, History history) {
        history.push(MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH));
    }
}
