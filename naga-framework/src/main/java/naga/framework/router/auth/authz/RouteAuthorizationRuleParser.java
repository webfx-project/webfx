package naga.framework.router.auth.authz;

import naga.framework.spi.authz.impl.inmemory.AuthorizationRuleType;
import naga.framework.spi.authz.impl.inmemory.InMemoryAuthorizationRule;
import naga.framework.spi.authz.impl.inmemory.parser.SimpleInMemoryAuthorizationRuleParserBase;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorizationRuleParser extends SimpleInMemoryAuthorizationRuleParserBase {

    @Override
    protected InMemoryAuthorizationRule parseAuthorization(AuthorizationRuleType type, String argument) {
        if (argument.startsWith("route:")) {
            String route = argument.substring(6).trim();
            boolean allowSubRoutes = route.endsWith("*");
            if (allowSubRoutes)
                route = route.substring(0, route.length() - 1);
            return new RouteAuthorizationRule(type, route, allowSubRoutes);
        }
        return null;
    }

}
