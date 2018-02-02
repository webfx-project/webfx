package mongoose.activities.backend.event.letters;

import mongoose.activities.backend.event.clone.CloneEventRouting;
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
public class LettersRouting {

    private static final String ANY_PATH = "/letters(/organization/:organizationId)?(/eventId/:eventId)?";
    private static final String EVENT_PATH = "/letters/eventId/:eventId";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PathBuilder.toRegexPath(ANY_PATH), true,
                false, LettersPresentationActivity::new, DomainPresentationActivityContextFinal::new, null
        );
    }

    public static void routeUsingEvent(Entity event, History history) {
        MongooseRoutingUtil.routeUsingEntityPrimaryKey(event, history, LettersRouting::routeUsingEventId);
    }

    public static void routeUsingEventId(Object eventId, History history) {
        history.push(MongooseRoutingUtil.interpolateEventIdInPath(eventId, EVENT_PATH));
    }
}
