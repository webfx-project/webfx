package naga.framework.router.auth.authz;

/**
 * @author Bruno Salmon
 */
public class RoutingRequest {

    private String routePath;

    public RoutingRequest() {
    }

    public RoutingRequest(String routePath) {
        this.routePath = routePath;
    }

    public String getRoutePath() {
        return routePath;
    }

    public RoutingRequest setRoutePath(String routePath) {
        this.routePath = routePath;
        return this;
    }
}
