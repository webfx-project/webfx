package mongoose.activities.shared.book.fees;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class FeesRouting {

    private final static String PATH = "/book/event/:eventId/fees";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , FeesActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    static String getFeesPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
