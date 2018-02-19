package naga.framework.spi.authz.impl.inmemory;

import naga.framework.spi.authz.impl.UserPrincipalAuthorizationService;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class InMemoryUserPrincipalAuthorizationService implements UserPrincipalAuthorizationService {

    private final Object userPrincipal;
    private final InMemoryAuthorizationRule inMemoryAuthorizationRules;

    public InMemoryUserPrincipalAuthorizationService(Object userPrincipal, InMemoryAuthorizationRule inMemoryAuthorizationRules) {
        this.userPrincipal = userPrincipal;
        this.inMemoryAuthorizationRules = inMemoryAuthorizationRules;
    }

    @Override
    public Object getUserPrincipal() {
        return userPrincipal;
    }

    @Override
    public Future<Boolean> isAuthorized(Object operationAuthorizationRequest) {
        return Future.succeededFuture(inMemoryAuthorizationRules.authorizes(operationAuthorizationRequest));
    }
}
