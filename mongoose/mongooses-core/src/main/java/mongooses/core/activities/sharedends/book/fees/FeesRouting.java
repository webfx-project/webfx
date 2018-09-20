package mongooses.core.activities.sharedends.book.fees;

import mongooses.core.activities.sharedends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class FeesRouting {

    private final static String PATH = "/book/event/:eventId/fees";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , FeesActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getFeesPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
