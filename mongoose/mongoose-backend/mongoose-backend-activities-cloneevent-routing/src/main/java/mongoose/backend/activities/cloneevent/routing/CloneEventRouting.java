package mongoose.backend.activities.cloneevent.routing;

import mongoose.client.util.routing.MongooseRoutingUtil;

/**
 * @author Bruno Salmon
 */
public final class CloneEventRouting {

    private static final String PATH = "/event/:eventId/clone";

    public static String getPath() {
        return PATH;
    }

    public static String getCloneEventPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, getPath());
    }

}
