package mongooses.core.activities.sharedends.book.summary;

import mongooses.core.activities.sharedends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class SummaryRouting {

    private final static String PATH = "/book/event/:eventId/summary";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , SummaryActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getSummaryPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
