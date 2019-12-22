package mongoose.backend.activities.statistics.routing;

import mongoose.client.util.routing.MongooseRoutingUtil;

/**
 * @author Bruno Salmon
 */
public final class StatisticsRouting {

    private final static String PATH = "/statistics/event/:eventId";

    public static String getPath() {
        return PATH;
    }

    public static String getEventStatisticsPath(Object eventId) {
        return MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH);
    }

}
