package mongoose.client.activities.unauthorized;

import webfx.framework.client.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.ProvidedUnauthorizedUiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class UnauthorizedRouting {

    private static final String PATH = "/unauthorized";

    public static String getPath() {
        return PATH;
    }

}
