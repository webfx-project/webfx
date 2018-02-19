package mongoose.authz;

import naga.framework.router.auth.authz.RouteAuthorizationRuleParser;
import naga.framework.router.auth.authz.RouteOperationAuthorizationRequestParser;
import naga.framework.spi.authz.impl.inmemory.InMemoryAuthorizationRuleRegistry;
import naga.framework.spi.authz.impl.inmemory.InMemoryUserPrincipalAuthorizationService;

/**
 * @author Bruno Salmon
 */
class InMemoryMongooseUserPrincipalAuthorizationService extends InMemoryUserPrincipalAuthorizationService {

    public InMemoryMongooseUserPrincipalAuthorizationService(Object userPrincipal) {
        super(userPrincipal, DEFAULT_AUTHORIZATION);
    }

    private static InMemoryAuthorizationRuleRegistry DEFAULT_AUTHORIZATION = new InMemoryAuthorizationRuleRegistry();
    static {
        DEFAULT_AUTHORIZATION.addOperationAuthorizationRequestParser(new RouteOperationAuthorizationRequestParser());
        DEFAULT_AUTHORIZATION.addInMemoryAuthorizationRuleParser(new RouteAuthorizationRuleParser());
        DEFAULT_AUTHORIZATION.registerInMemoryAuthorizationRule("route: /monitor");
        DEFAULT_AUTHORIZATION.registerInMemoryAuthorizationRule("route: /tester");
        DEFAULT_AUTHORIZATION.registerInMemoryAuthorizationRule("route: /bookings/*");
    }

}
