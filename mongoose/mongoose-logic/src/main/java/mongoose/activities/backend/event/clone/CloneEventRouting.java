package mongoose.activities.backend.event.clone;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class CloneEventRouting {

    private static final String PATH = "/event/:eventId/clone";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , CloneEventPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

    static String getCloneEventPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, getPath());
    }
}
