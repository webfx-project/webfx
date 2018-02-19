package naga.framework.spi.authz.impl;

import naga.framework.spi.authz.AuthorizationService;
import naga.util.async.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class AuthorizationServiceBase implements AuthorizationService {

    private final Map<Object, UserPrincipalAuthorizationService> cache = new HashMap<>();

    @Override
    public Future<Boolean> isAuthorized(Object operationAuthorizationRequest, Object userPrincipal) {
        return getOrCreateUserPrincipalAuthorizationService(userPrincipal).isAuthorized(operationAuthorizationRequest);
    }

    protected UserPrincipalAuthorizationService getOrCreateUserPrincipalAuthorizationService(Object userPrincipal) {
        UserPrincipalAuthorizationService userPrincipalAuthorizationService = cache.get(userPrincipal);
        if (userPrincipalAuthorizationService == null)
            cache.put(userPrincipal, userPrincipalAuthorizationService = createUserPrincipalAuthorizationService(userPrincipal));
        return userPrincipalAuthorizationService;
    }

    protected abstract UserPrincipalAuthorizationService createUserPrincipalAuthorizationService(Object userPrincipal);
}
