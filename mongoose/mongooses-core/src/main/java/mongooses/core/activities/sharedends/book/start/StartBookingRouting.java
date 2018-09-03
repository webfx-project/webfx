package mongooses.core.activities.sharedends.book.start;

import mongooses.core.activities.sharedends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class StartBookingRouting {

    private final static String PATH = "/book/event/:eventId/start";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , StartBookingActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getStartBookingPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
