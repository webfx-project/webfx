package mongoose.activities.bothends.book.program;

import mongoose.activities.bothends.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public class ProgramRouting {

    private final static String PATH = "/book/event/:eventId/program";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , ProgramActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getProgramPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
