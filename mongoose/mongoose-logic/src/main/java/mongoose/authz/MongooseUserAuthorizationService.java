package mongoose.authz;

import naga.framework.router.auth.authz.RouteAuthorizationRuleParser;
import naga.framework.router.auth.authz.RouteOperationAuthorizationRequestParser;
import naga.platform.services.authz.impl.AuthorizationRegistry;
import naga.platform.services.authz.impl.LoadedAuthorizationsUser;

/**
 * @author Bruno Salmon
 */
class MongooseUserAuthorizationService extends LoadedAuthorizationsUser {

    public MongooseUserAuthorizationService(Object userPrincipal) {
        super(userPrincipal, DEFAULT_AUTHORIZATION);
    }

    private static AuthorizationRegistry DEFAULT_AUTHORIZATION = new AuthorizationRegistry();
    static {
        DEFAULT_AUTHORIZATION.addOperationAuthorizationRequestParser(new RouteOperationAuthorizationRequestParser());
        DEFAULT_AUTHORIZATION.addAuthorizationRuleParser(new RouteAuthorizationRuleParser());
        DEFAULT_AUTHORIZATION.registerAuthorization("route: /monitor");
        DEFAULT_AUTHORIZATION.registerAuthorization("route: /tester");
        DEFAULT_AUTHORIZATION.registerAuthorization("route: /bookings/*");
    }

}
