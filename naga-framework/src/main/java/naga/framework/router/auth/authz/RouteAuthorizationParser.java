package naga.framework.router.auth.authz;

import naga.platform.services.auth.spi.authz.Authorization;
import naga.platform.services.auth.spi.authz.AuthorizationParser;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorizationParser implements AuthorizationParser {

    @Override
    public Authorization parseAuthorization(String authorization) {
        if (authorization.startsWith("route:")) {
            String route = authorization.substring(6).trim();
            boolean allowSubRoutes = route.endsWith("*");
            if (allowSubRoutes)
                route = route.substring(0, route.length() - 1);
            return new RouteAuthorization(route, allowSubRoutes);
        }
        return null;
    }

}
