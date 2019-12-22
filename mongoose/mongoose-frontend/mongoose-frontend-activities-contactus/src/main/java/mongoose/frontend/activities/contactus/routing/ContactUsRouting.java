package mongoose.frontend.activities.contactus.routing;

import mongoose.client.util.routing.MongooseRoutingUtil;

/**
 * @author Bruno Salmon
 */
public final class ContactUsRouting {

    private final static String PATH = "/contact-us/:documentId";

    public static String getPath() {
        return PATH;
    }

    public static String getContactUsPath(Object documentId) {
        return MongooseRoutingUtil.interpolateDocumentIdInPath(documentId, getPath());
    }

}
