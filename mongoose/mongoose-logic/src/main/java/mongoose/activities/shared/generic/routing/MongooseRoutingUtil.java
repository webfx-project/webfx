package mongoose.activities.shared.generic.routing;

import naga.framework.orm.entity.Entity;
import naga.platform.client.url.history.History;
import naga.util.Strings;
import naga.util.function.BiConsumer;

/**
 * @author Bruno Salmon
 */
public class MongooseRoutingUtil {

    public static void routeUsingEntityPrimaryKey(Entity entity, History history, BiConsumer<Object, History> entityIdRouter) {
        if (entity != null)
            entityIdRouter.accept(entity.getPrimaryKey(), history);
    }

    public static String interpolateParamInPath(String paramToken, Object paramValue, String path) {
        return Strings.replaceAll(path, paramToken, paramValue);
    }
    public static String interpolateEventIdInPath(Object eventId, String path) {
        return interpolateParamInPath(":eventId", eventId, path);
    }

    public static String interpolateOrganizationIdInPath(Object eventId, String path) {
        return interpolateParamInPath(":organizationId", eventId, path);
    }

    public static String interpolateLetterIdInPath(Object letterId, String path) {
        return interpolateParamInPath(":letterId", letterId, path);
    }

    public static String interpolateCartUuidInPath(Object cartUuid, String path) {
        return interpolateParamInPath(":cartUuid", cartUuid, path);
    }
}
