package naga.framework.router.auth.authz;

import naga.framework.spi.authz.impl.inmemory.parser.AuthorizationRequestParser;

/**
 * @author Bruno Salmon
 */
public class RouteAuthorizationRequestParser implements AuthorizationRequestParser {

    @Override
    public Object parseAuthorizationRequest(String authorizationRequest) {
        if (authorizationRequest.startsWith("route:"))
            return new RouteAuthorizationRequest(authorizationRequest.substring(6).trim());
        return authorizationRequest;
    }

}
