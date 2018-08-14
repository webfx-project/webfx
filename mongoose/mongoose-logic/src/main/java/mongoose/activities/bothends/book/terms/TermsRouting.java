package mongoose.activities.bothends.book.terms;

import mongoose.activities.bothends.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public class TermsRouting {

    private final static String PATH = "/book/event/:eventId/terms";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , TermsActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getTermsPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
