package mongoose.client.activities.generic.routing;

import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.platform.shared.util.Strings;

/**
 * @author Bruno Salmon
 */
public final class MongooseRoutingUtil {

    public static String interpolateParamInPath(String paramToken, Object paramValue, String path) {
        return paramValue == null ? null : Strings.replaceAll(path, paramToken, paramValue);
    }

    public static String interpolateEventIdInPath(Object eventId, String path) {
        return interpolateParamInPath(":eventId", toPk(eventId), path);
    }

    public static String interpolateOrganizationIdInPath(Object organizationId, String path) {
        return interpolateParamInPath(":organizationId", toPk(organizationId), path);
    }

    public static String interpolateLetterIdInPath(Object letterId, String path) {
        return interpolateParamInPath(":letterId", toPk(letterId), path);
    }

    public static String interpolateCartUuidInPath(Object cartUuid, String path) {
        return interpolateParamInPath(":cartUuid", cartUuid, path);
    }

    public static String interpolateDocumentIdInPath(Object documentId, String path) {
        return interpolateParamInPath(":documentId", toPk(documentId), path);
    }

    public static Object toPk(Object id) {
        if (id instanceof Entity)
            return ((Entity) id).getId().getPrimaryKey();
        if (id instanceof EntityId)
            return ((EntityId) id).getPrimaryKey();
        return id;
    }
}
