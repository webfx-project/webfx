package mongoose.activities.bothends.book.person;

import mongoose.activities.bothends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class PersonRouting {

    private final static String PATH = "/book/event/:eventId/person";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , PersonActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getPersonPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
