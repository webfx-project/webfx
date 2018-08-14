package mongoose.activities.backend.letters;

import mongoose.activities.bothends.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.router.util.PathBuilder;
import naga.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public class LettersRouting {

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
