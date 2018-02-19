package mongoose.authz;

import naga.framework.spi.authz.impl.AuthorizationServiceProviderBase;
import naga.framework.spi.authz.impl.UserPrincipalAuthorizationChecker;

/**
 * @author Bruno Salmon
 */
public class MongooseAuthorizationServiceProvider extends AuthorizationServiceProviderBase {

    @Override
    protected UserPrincipalAuthorizationChecker createUserPrincipalAuthorizationChecker(Object userPrincipal) {
        return new MongooseInMemoryUserPrincipalAuthorizationChecker(userPrincipal);
    }
}
