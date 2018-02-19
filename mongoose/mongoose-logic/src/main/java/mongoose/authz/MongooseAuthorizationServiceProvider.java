package mongoose.authz;

import naga.framework.spi.authz.impl.AuthorizationServiceProviderBase;
import naga.framework.spi.authz.impl.UserPrincipalAuthorizationService;

/**
 * @author Bruno Salmon
 */
public class MongooseAuthorizationServiceProvider extends AuthorizationServiceProviderBase {

    @Override
    protected UserPrincipalAuthorizationService createUserPrincipalAuthorizationService(Object userPrincipal) {
        return new InMemoryMongooseUserPrincipalAuthorizationService(userPrincipal);
    }
}
