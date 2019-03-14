package mongoose.frontend.activities.summary.routing;

import mongoose.client.util.routing.MongooseRoutingUtil;

/**
 * @author Bruno Salmon
 */
public final class SummaryRouting {

    private final static String PATH = "/book/event/:eventId/summary";

    public static String getPath() {
        return PATH;
    }

    public static String getSummaryPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }

}
