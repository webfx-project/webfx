package mongoose.activities.backend.event.clone;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class CloneEventRouting {

    public static final String PATH = "/event/:eventId/clone";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PATH, false,
                false, CloneEventPresentationActivity::new, DomainPresentationActivityContextFinal::new, null
        );
    }

    public static void routeUsingEvent(Entity event, History history) {
        MongooseRoutingUtil.routeUsingEntityId(event, history, CloneEventRouting::routeUsingEventId);
    }

    public static void routeUsingEventId(Object eventId, History history) {
        history.push(MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH));
    }

}
