package naga.platform.services.authz.impl;

import naga.platform.services.authz.spi.AuthorizationService;
import naga.util.async.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class AuthorizationServiceBase implements AuthorizationService {

    private final Map<Object, UserPrincipalAuthorizationService> userPrincipalAuthorizationServices = new HashMap<>();

    @Override
    public Future<Boolean> isAuthorized(Object operationAuthorizationRequest, Object userPrincipal) {
        return getOrCreateUserPrincipalAuthorizationService(userPrincipal).isAuthorized(operationAuthorizationRequest);
    }

    protected UserPrincipalAuthorizationService getOrCreateUserPrincipalAuthorizationService(Object userPrincipal) {
        UserPrincipalAuthorizationService userPrincipalAuthorizationService = userPrincipalAuthorizationServices.get(userPrincipal);
        if (userPrincipalAuthorizationService == null)
            userPrincipalAuthorizationServices.put(userPrincipal, userPrincipalAuthorizationService = createUserPrincipalAuthorizationService(userPrincipal));
        return userPrincipalAuthorizationService;
    }

    protected abstract UserPrincipalAuthorizationService createUserPrincipalAuthorizationService(Object userPrincipal);
}
