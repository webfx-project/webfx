package naga.framework.router.auth.authz;

import naga.framework.spi.authz.impl.inmemory.AuthorizationRuleType;
import naga.framework.spi.authz.impl.inmemory.SimpleInMemoryAuthorizationRuleBase;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorizationRule extends SimpleInMemoryAuthorizationRuleBase<RouteAuthorizationRequest> {

    private final String route;
    private final boolean includeSubRoutes;

    public RouteAuthorizationRule(AuthorizationRuleType type, String route, boolean includeSubRoutes) {
        super(type, RouteAuthorizationRequest.class);
        this.route = route;
        this.includeSubRoutes = includeSubRoutes;
    }

    @Override
    protected boolean matchRule(RouteAuthorizationRequest operationAuthorizationRequest) {
        String requestedRoute = operationAuthorizationRequest.getRequestedRoute();
        return requestedRoute.equals(route) || includeSubRoutes && requestedRoute.startsWith(route);
    }
}
