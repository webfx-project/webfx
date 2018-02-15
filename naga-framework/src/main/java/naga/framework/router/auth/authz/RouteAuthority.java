package naga.framework.router.auth.authz;

/**
 * @author Bruno Salmon
 */
public class RouteAuthority {

    private final String requestedRoute;

    public RouteAuthority(String requestedRoute) {
        this.requestedRoute = requestedRoute;
    }

    public String getRequestedRoute() {
        return requestedRoute;
    }
}
