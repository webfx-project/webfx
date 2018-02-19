package mongoose.authz;

import naga.platform.services.authz.impl.AuthorizationServiceBase;
import naga.platform.services.authz.impl.UserPrincipalAuthorizationService;

/**
 * @author Bruno Salmon
 */
public class MongooseAuthorizationService extends AuthorizationServiceBase {

    @Override
    protected UserPrincipalAuthorizationService createUserPrincipalAuthorizationService(Object userPrincipal) {
        return new MongooseUserAuthorizationService(userPrincipal);
    }
}
