package mongoose.activities.shared.generic.routing;

import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;
import naga.platform.client.url.history.History;
import naga.util.Strings;
import naga.util.function.BiConsumer;

/**
 * @author Bruno Salmon
 */
public class MongooseRoutingUtil {

    public static void routeUsingEntityId(Entity entity, History history, BiConsumer<Object, History> entityIdRouter) {
        if (entity != null)
            entityIdRouter.accept(entity.getPrimaryKey(), history);
    }

    public static String interpolateParamInPath(String paramToken, Object paramValue, String path) {
        return paramValue == null ? null : Strings.replaceAll(path, paramToken, paramValue);
    }

    public static String interpolateEventIdInPath(Object eventId, String path) {
        return interpolateParamInPath(":eventId", toPk(eventId), path);
    }

    public static String interpolateOrganizationIdInPath(Object eventId, String path) {
        return interpolateParamInPath(":organizationId", toPk(eventId), path);
    }

    public static String interpolateLetterIdInPath(Object letterId, String path) {
        return interpolateParamInPath(":letterId", toPk(letterId), path);
    }

    public static String interpolateCartUuidInPath(Object cartUuid, String path) {
        return interpolateParamInPath(":cartUuid", cartUuid, path);
    }

    public static Object toPk(Object id) {
        if (id instanceof Entity)
            return ((Entity) id).getId().getPrimaryKey();
        if (id instanceof EntityId)
            return ((EntityId) id).getPrimaryKey();
        return id;
    }
}
