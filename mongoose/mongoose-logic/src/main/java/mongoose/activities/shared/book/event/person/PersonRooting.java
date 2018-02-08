package mongoose.activities.shared.book.event.person;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class PersonRooting {

    private final static String PATH = "/book/event/:eventId/person";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PATH, false,
                false, PersonViewActivity::new, ViewDomainActivityContextFinal::new, null
        );
    }

    public static void routeUsingEvent(Entity event, History history) {
        MongooseRoutingUtil.routeUsingEntityId(event, history, PersonRooting::routeUsingEventId);
    }

    public static void routeUsingEventId(Object eventId, History history) {
        history.push(MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH));
    }

}
