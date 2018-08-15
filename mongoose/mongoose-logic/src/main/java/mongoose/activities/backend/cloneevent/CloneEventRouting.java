package mongoose.activities.backend.cloneevent;

import mongoose.activities.bothends.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public class CloneEventRouting {

    private static final String PATH = "/event/:eventId/clone";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , CloneEventActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

    public static String getCloneEventPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, getPath());
    }
}
