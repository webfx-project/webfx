package naga.framework.router.auth.authz;

import naga.platform.services.auth.spi.authz.Authorization;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorization implements Authorization<RouteAuthority> {

    private final String authorizedRoute;
    private final boolean allowSubRoutes;

    public RouteAuthorization(String authorizedRoute, boolean allowSubRoutes) {
        this.authorizedRoute = authorizedRoute;
        this.allowSubRoutes = allowSubRoutes;
    }

    @Override
    public boolean authorizes(RouteAuthority authority) {
        String requestedRoute = authority.getRequestedRoute();
        if (requestedRoute.equals(authorizedRoute))
            return true;
        if (allowSubRoutes && requestedRoute.startsWith(authorizedRoute))
            return true;
        return false;
    }

    @Override
    public Class<RouteAuthority> authorityClass() {
        return RouteAuthority.class;
    }
}
