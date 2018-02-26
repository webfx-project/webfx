package naga.framework.router.auth.authz;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorizationRequest {

    private final String routePath;

    public RouteAuthorizationRequest(String routePath) {
        this.routePath = routePath;
    }

    public String getRoutePath() {
        return routePath;
    }
}
