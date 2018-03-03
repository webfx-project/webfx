package mongoose.activities.backend.monitor;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.router.auth.authz.RoutingRequest;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class MonitorRouting {

    final static String PATH = "/monitor";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , true
                , MonitorPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static RoutingRequest routingRequest() {
        return new RoutingRequest(PATH);
    }

}
