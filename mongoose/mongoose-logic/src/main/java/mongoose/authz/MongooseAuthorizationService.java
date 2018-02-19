package mongoose.authz;

import naga.framework.spi.authz.impl.AuthorizationServiceBase;
import naga.framework.spi.authz.impl.UserPrincipalAuthorizationService;

/**
 * @author Bruno Salmon
 */
public class MongooseAuthorizationService extends AuthorizationServiceBase {

    @Override
    protected UserPrincipalAuthorizationService createUserPrincipalAuthorizationService(Object userPrincipal) {
        return new MongooseUserAuthorizationService(userPrincipal);
    }
}
