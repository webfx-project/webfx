package mongoose.activities.backend.monitor;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class MonitorRooting {

    private final static String PATH = "/monitor";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PATH, false,
                false, MonitorPresentationActivity::new, DomainPresentationActivityContextFinal::new, null
        );
    }

    public static void route(History history) {
        history.push(PATH);
    }

}
