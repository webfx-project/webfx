package mongooses.core.activities.backend.letters;

import mongooses.core.activities.sharedends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.router.util.PathBuilder;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class LettersRouting {

    private static final String ANY_PATH = "/letters(/organization/:organizationId)?(/event/:eventId)?";
    private static final String EVENT_PATH = "/letters/event/:eventId";

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(
                PathBuilder.toRegexPath(ANY_PATH)
                , false
                , LettersActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getEventLettersPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, EVENT_PATH);
    }
}
