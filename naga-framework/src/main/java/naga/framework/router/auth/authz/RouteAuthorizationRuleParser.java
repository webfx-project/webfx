package naga.framework.router.auth.authz;

import naga.framework.spi.authz.impl.Authorization;
import naga.framework.spi.authz.impl.AuthorizationRuleParser;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorizationRuleParser implements AuthorizationRuleParser {

    @Override
    public Authorization parseAuthorization(String authorizationRule) {
        if (authorizationRule.startsWith("route:")) {
            String route = authorizationRule.substring(6).trim();
            boolean allowSubRoutes = route.endsWith("*");
            if (allowSubRoutes)
                route = route.substring(0, route.length() - 1);
            return new RouteAuthorization(route, allowSubRoutes);
        }
        return null;
    }

}
