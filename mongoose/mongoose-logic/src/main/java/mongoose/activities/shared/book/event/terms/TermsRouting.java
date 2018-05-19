package mongoose.activities.shared.book.event.terms;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class TermsRouting {

    private final static String PATH = "/book/event/:eventId/terms";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , TermsPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    static String getTermsPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
