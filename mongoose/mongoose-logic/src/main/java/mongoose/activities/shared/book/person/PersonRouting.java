package mongoose.activities.shared.book.person;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

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

    static String getPersonPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
