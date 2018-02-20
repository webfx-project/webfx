package naga.framework.router.auth.authz;

import naga.framework.spi.authz.impl.inmemory.AuthorizationRuleType;
import naga.framework.spi.authz.impl.inmemory.InMemoryAuthorizationRule;
import naga.framework.spi.authz.impl.inmemory.parser.InMemoryAuthorizationRuleParser;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorizationRuleParser implements InMemoryAuthorizationRuleParser {

    @Override
    public InMemoryAuthorizationRule parseAuthorization(String authorizationRule) {
        if (authorizationRule.startsWith("route:")) {
            String route = authorizationRule.substring(6).trim();
            boolean allowSubRoutes = route.endsWith("*");
            if (allowSubRoutes)
                route = route.substring(0, route.length() - 1);
            return new RouteAuthorizationRule(AuthorizationRuleType.GRANT, route, allowSubRoutes);
        }
        return null;
    }

}
