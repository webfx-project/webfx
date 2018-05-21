package mongoose.activities.shared.book.options;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class OptionsRouting {

    private static final String PATH = "/book/event/:eventId/options";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , OptionsActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

    public static String getEventOptionsPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, getPath());
    }
}
