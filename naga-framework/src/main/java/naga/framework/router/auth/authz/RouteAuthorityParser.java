package naga.framework.router.auth.authz;

import naga.platform.services.auth.spi.authz.AuthorityParser;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorityParser implements AuthorityParser {

    @Override
    public Object parseAuthority(String authority) {
        if (authority.startsWith("route:"))
            return new RouteAuthority(authority.substring(6).trim());
        return authority;
    }

}
