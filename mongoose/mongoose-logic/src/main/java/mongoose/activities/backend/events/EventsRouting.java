package mongoose.activities.backend.events;

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
public class EventsRouting {

    private final static String ANY_PATH = "/events(/organization/:organizationId)?";
    private final static String ALL_EVENTS_PATH = "/events";
    private final static String ORGANIZATION_PATH = "/events/organization/:organizationId";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PathBuilder.toRegexPath(ANY_PATH), true,
                false, EventsPresentationActivity::new, DomainPresentationActivityContextFinal::new, null
        );
    }

    public static void route(History history) {
        history.push(ALL_EVENTS_PATH);
    }

    public static void routeUsingOrganization(Entity organization, History history) {
        MongooseRoutingUtil.routeUsingEntityPrimaryKey(organization, history, EventsRouting::routeUsingOrganizationId);
    }

    public static void routeUsingOrganizationId(Object organizationId, History history) {
        history.push(MongooseRoutingUtil.interpolateOrganizationIdInPath(organizationId, ORGANIZATION_PATH));
    }
}
