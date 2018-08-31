package mongoose.activities.bothends.book.program;

import mongoose.activities.bothends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class ProgramRouting {

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
