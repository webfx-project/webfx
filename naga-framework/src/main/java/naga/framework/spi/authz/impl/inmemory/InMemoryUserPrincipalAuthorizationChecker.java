package naga.framework.spi.authz.impl.inmemory;

import naga.framework.spi.authz.impl.UserPrincipalAuthorizationChecker;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class InMemoryUserPrincipalAuthorizationChecker implements UserPrincipalAuthorizationChecker {

    private final Object userPrincipal;
    private final InMemoryAuthorizationRule inMemoryAuthorizationRules;

    public InMemoryUserPrincipalAuthorizationChecker(Object userPrincipal, InMemoryAuthorizationRule inMemoryAuthorizationRules) {
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
