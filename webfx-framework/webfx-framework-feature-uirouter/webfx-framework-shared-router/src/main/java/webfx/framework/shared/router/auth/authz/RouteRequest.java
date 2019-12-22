package webfx.framework.shared.router.auth.authz;

/**
 * @author Bruno Salmon
 */
public class RouteRequest {

    private final String routePath;

    public RouteRequest(String routePath) {
        this.routePath = routePath;
    }

    public String getRoutePath() {
        return routePath;
    }

}
