package mongoose.activities.bothends.book.terms;

import mongoose.activities.bothends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class TermsRouting {

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
