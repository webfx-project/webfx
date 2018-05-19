package mongoose.activities.shared.book.event.summary;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class SummaryRouting {

    private final static String PATH = "/book/event/:eventId/summary";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , SummaryActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    static String getSummaryPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
