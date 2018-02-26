package mongoose.activities.backend.tester;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.router.auth.authz.RouteAuthorizationRequest;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class TesterRouting {

    private final static String PATH = "/tester";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , TesterPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static AsyncFunction<TesterRoutingRequest, Void> executor() {
        return request -> Future.runAsync(() -> route(request.getHistory()));
    }

    public static RouteAuthorizationRequest authorizationRequest() {
        return new RouteAuthorizationRequest(PATH);
    }

    public static void route(History history) {
        history.push(PATH);
    }

}
