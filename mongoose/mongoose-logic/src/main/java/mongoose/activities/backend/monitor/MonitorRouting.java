package mongoose.activities.backend.monitor;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.router.auth.authz.RouteAuthorizationRequest;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;
import naga.util.async.AsyncFunction;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class MonitorRouting {

    private final static String PATH = "/monitor";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , true
                , MonitorPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static AsyncFunction<MonitorRoutingRequest, Void> executor() {
        return request -> Future.runAsync(() -> route(request.getHistory()));
    }

    public static RouteAuthorizationRequest authorizationRequest() {
        return new RouteAuthorizationRequest(PATH);
    }

    public static void route(History history) {
        history.push(PATH);
    }

}
