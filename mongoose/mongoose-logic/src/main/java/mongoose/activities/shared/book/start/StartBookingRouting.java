package mongoose.activities.shared.book.start;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class StartBookingRouting {

    private final static String PATH = "/book/event/:eventId/start";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , StartBookingActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    static String getStartBookingPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
