package mongoose.activities.shared.book.event.program;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

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

    static String getProgramPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }
}
