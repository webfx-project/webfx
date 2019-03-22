package mongoose.client.activities.unauthorized;

import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;

/**
 * @author Bruno Salmon
 */
public final class UnauthorizedRouting {

    private static final String PATH = "/unauthorized";

    public static String getPath() {
        return PATH;
    }

}
