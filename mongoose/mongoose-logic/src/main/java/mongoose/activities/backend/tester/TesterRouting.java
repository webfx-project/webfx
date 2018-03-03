package mongoose.activities.backend.tester;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.router.auth.authz.RoutingRequest;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class TesterRouting {

    final static String PATH = "/tester";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , TesterPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static RoutingRequest routingRequest() {
        return new RoutingRequest(PATH);
    }

}
