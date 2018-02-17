package naga.framework.router.auth.authz;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorizationRequest {

    private final String requestedRoute;

    public RouteAuthorizationRequest(String requestedRoute) {
        this.requestedRoute = requestedRoute;
    }

    public String getRequestedRoute() {
        return requestedRoute;
    }
}
