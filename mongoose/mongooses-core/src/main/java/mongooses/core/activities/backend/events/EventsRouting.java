package mongooses.core.activities.backend.events;

import mongooses.core.activities.sharedends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.router.util.PathBuilder;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class EventsRouting {

    private final static String ANY_PATH = "/events(/organization/:organizationId)?";
    private final static String ALL_EVENTS_PATH = "/events";
    private final static String ORGANIZATION_PATH = "/events/organization/:organizationId";

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(
                PathBuilder.toRegexPath(ANY_PATH)
                , false
                , EventsActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getAllEventsPath() {
        return ALL_EVENTS_PATH;
    }

    public static String getOrganizationEventsPath(Object organizationId) {
        return MongooseRoutingUtil.interpolateOrganizationIdInPath(organizationId, ORGANIZATION_PATH);
    }
}
