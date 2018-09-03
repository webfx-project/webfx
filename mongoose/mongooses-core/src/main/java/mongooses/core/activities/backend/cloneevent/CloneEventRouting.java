package mongooses.core.activities.backend.cloneevent;

import mongooses.core.activities.sharedends.generic.routing.MongooseRoutingUtil;
import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class CloneEventRouting {

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
