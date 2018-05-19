package naga.framework.router.auth.authz;

/**
 * @author Bruno Salmon
 */
public class RoutingRequest {

    private final String routePath;

    public RoutingRequest(String routePath) {
        this.routePath = routePath;
    }

    public String getRoutePath() {
        return routePath;
    }

}
