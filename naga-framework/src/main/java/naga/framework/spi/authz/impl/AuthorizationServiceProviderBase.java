package naga.framework.spi.authz.impl;

import naga.framework.spi.authz.AuthorizationServiceProvider;
import naga.util.async.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class AuthorizationServiceProviderBase implements AuthorizationServiceProvider {

    private final Map<Object, UserPrincipalAuthorizationChecker> cache = new HashMap<>();

    @Override
    public Future<Boolean> isAuthorized(Object operationAuthorizationRequest, Object userPrincipal) {
        if (userPrincipal == null)
            return Future.failedFuture(new IllegalArgumentException("userPrincipal must be not null"));
        return getOrCreateUserPrincipalAuthorizationChecker(userPrincipal).isAuthorized(operationAuthorizationRequest);
    }

    protected UserPrincipalAuthorizationChecker getOrCreateUserPrincipalAuthorizationChecker(Object userPrincipal) {
        UserPrincipalAuthorizationChecker userPrincipalAuthorizationChecker = cache.get(userPrincipal);
        if (userPrincipalAuthorizationChecker == null)
            cache.put(userPrincipal, userPrincipalAuthorizationChecker = createUserPrincipalAuthorizationChecker(userPrincipal));
        return userPrincipalAuthorizationChecker;
    }

    protected abstract UserPrincipalAuthorizationChecker createUserPrincipalAuthorizationChecker(Object userPrincipal);
}
