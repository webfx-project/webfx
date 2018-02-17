package mongoose.auth.authz;

import naga.framework.router.auth.authz.RouteOperationAuthorizationRequestParser;
import naga.framework.router.auth.authz.RouteAuthorizationRuleParser;
import naga.platform.services.auth.spi.authz.AuthorizationRegistry;
import naga.platform.services.auth.spi.authz.LoadedAuthorizationsUser;

/**
 * @author Bruno Salmon
 */
public class MongooseUser extends LoadedAuthorizationsUser {

    private final Object userAccountId;

    public MongooseUser(Object userAccountId) {
        super(DEFAULT_AUTHORIZATION);
        this.userAccountId = userAccountId;
    }

    public Object getUserAccountId() {
        return userAccountId;
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
