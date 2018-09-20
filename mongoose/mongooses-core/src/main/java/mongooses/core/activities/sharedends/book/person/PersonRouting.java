package mongooses.core.activities.sharedends.book.person;

import mongooses.core.activities.sharedends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
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
