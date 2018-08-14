package naga.framework.services.authz.spi.impl;

import naga.framework.services.authz.spi.AuthorizationServiceProvider;
import naga.util.async.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class AuthorizationServiceProviderImplBase implements AuthorizationServiceProvider {

    private final Map<Object, UserPrincipalAuthorizationChecker> cache = new HashMap<>();

    @Override
    public Future<Boolean> isAuthorized(Object operationAuthorizationRequest, Object userPrincipal) {
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
