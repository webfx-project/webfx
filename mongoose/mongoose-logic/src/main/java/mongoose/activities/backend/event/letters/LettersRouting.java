package mongoose.activities.backend.event.letters;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.router.util.PathBuilder;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class LettersRouting {

    private static final String ANY_PATH = "/letters(/organization/:organizationId)?(/eventId/:eventId)?";
    static final String EVENT_PATH = "/letters/eventId/:eventId";

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(
                PathBuilder.toRegexPath(ANY_PATH)
                , false
                , LettersPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

}
