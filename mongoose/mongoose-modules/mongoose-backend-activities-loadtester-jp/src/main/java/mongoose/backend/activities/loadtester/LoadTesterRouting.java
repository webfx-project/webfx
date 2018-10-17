package mongoose.backend.activities.loadtester;

import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class LoadTesterRouting {

    private final static String PATH = "/load-tester";

    public static String getPath() {
        return PATH;
    }

}
