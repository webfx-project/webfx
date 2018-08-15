package mongoose.activities.bothends.book.person;

import mongoose.activities.bothends.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public class PersonRouting {

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
