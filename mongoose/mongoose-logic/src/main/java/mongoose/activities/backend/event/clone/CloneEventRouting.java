package mongoose.activities.backend.event.clone;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class CloneEventRouting {

    public static final String PATH = "/event/:eventId/clone";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , CloneEventPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static AsyncFunction<CloneEventRoutingRequest, Void> executor() {
        return request -> Future.runAsync(() -> routeUsingEventId(request.getEventId(), request.getHistory()));
    }

    public static void routeUsingEvent(Entity event, History history) {
        MongooseRoutingUtil.routeUsingEntityId(event, history, CloneEventRouting::routeUsingEventId);
    }

    public static void routeUsingEventId(Object eventId, History history) {
        history.push(MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH));
    }

}
