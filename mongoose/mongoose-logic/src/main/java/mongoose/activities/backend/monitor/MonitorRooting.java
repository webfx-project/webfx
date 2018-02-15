package mongoose.activities.backend.monitor;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class MonitorRooting {

    private final static String PATH = "/monitor";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , MonitorPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static void route(History history) {
        history.push(PATH);
    }

}
