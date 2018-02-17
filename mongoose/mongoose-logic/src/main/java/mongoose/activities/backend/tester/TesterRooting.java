package mongoose.activities.backend.tester;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.router.auth.authz.RouteAuthorizationRequest;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class TesterRooting {

    private final static String PATH = "/tester";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , TesterPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static RouteAuthorizationRequest authorizationRequest() {
        return new RouteAuthorizationRequest(PATH);
    }

    public static void route(History history) {
        history.push(PATH);
    }

}
