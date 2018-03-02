package naga.framework.router.auth.authz;

import naga.framework.spi.authz.impl.inmemory.AuthorizationRuleType;
import naga.framework.spi.authz.impl.inmemory.SimpleInMemoryAuthorizationRuleBase;

/**
 * @author Bruno Salmon
 */
public class RoutingAuthorizationRule extends SimpleInMemoryAuthorizationRuleBase<RoutingRequest> {

    private final String route;
    private final boolean includeSubRoutes;

    public RoutingAuthorizationRule(AuthorizationRuleType type, String route, boolean includeSubRoutes) {
        super(type, RoutingRequest.class);
        this.route = route;
        this.includeSubRoutes = includeSubRoutes;
    }

    @Override
    protected boolean matchRule(RoutingRequest operationRequest) {
        String requestedRoute = operationRequest.getRoutePath();
        return requestedRoute.equals(route) || includeSubRoutes && requestedRoute.startsWith(route);
    }
}
