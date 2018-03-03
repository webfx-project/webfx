package mongoose.activities.backend.events;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.router.util.PathBuilder;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class EventsRouting {

    private final static String ANY_PATH = "/events(/organization/:organizationId)?";
    final static String ALL_EVENTS_PATH = "/events";
    final static String ORGANIZATION_PATH = "/events/organization/:organizationId";

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(
                PathBuilder.toRegexPath(ANY_PATH)
                , false
                , EventsPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }
    
}
