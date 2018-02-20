package naga.framework.router.auth.authz;

import naga.framework.spi.authz.impl.inmemory.AuthorizationRuleResult;
import naga.framework.spi.authz.impl.inmemory.InMemoryAuthorizationRule;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorizationRule implements InMemoryAuthorizationRule<RouteAuthorizationRequest> {

    private final String authorizedRoute;
    private final boolean allowSubRoutes;

    public RouteAuthorizationRule(String authorizedRoute, boolean allowSubRoutes) {
        this.authorizedRoute = authorizedRoute;
        this.allowSubRoutes = allowSubRoutes;
    }

    @Override
    public AuthorizationRuleResult computeRuleResult(RouteAuthorizationRequest operationAuthorizationRequest) {
        String requestedRoute = operationAuthorizationRequest.getRequestedRoute();
        if (requestedRoute.equals(authorizedRoute) || (allowSubRoutes && requestedRoute.startsWith(authorizedRoute)))
            return AuthorizationRuleResult.GRANTED;
        return AuthorizationRuleResult.OUT_OF_RULE_CONTEXT;
    }

    @Override
    public Class<RouteAuthorizationRequest> operationAuthorizationRequestClass() {
        return RouteAuthorizationRequest.class;
    }
}
