package mongooses.core.backend.activities.letters;

import mongooses.core.sharedends.activities.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.router.util.PathBuilder;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.framework.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class LettersRouting {

    private static final String ANY_PATH = "/letters(/organization/:organizationId)?(/event/:eventId)?";
    private static final String EVENT_PATH = "/letters/event/:eventId";

    public static String getEventLettersPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, EVENT_PATH);
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(
                PathBuilder.toRegexPath(ANY_PATH)
                , false
                , LettersActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static final class ProvidedUiRoute extends UiRouteImpl {
        public ProvidedUiRoute() {
            super(uiRoute());
        }
    }

}
