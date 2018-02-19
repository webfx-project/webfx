package naga.platform.services.authz.impl;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class LoadedAuthorizationsUser implements UserPrincipalAuthorizationService {

    private final Object userPrincipal;
    private final Authorization userLoadedAuthorizations;

    public LoadedAuthorizationsUser(Object userPrincipal, Authorization userLoadedAuthorizations) {
        this.userPrincipal = userPrincipal;
        this.userLoadedAuthorizations = userLoadedAuthorizations;
    }

    @Override
    public Object getUserPrincipal() {
        return userPrincipal;
    }

    @Override
    public Future<Boolean> isAuthorized(Object operationAuthorizationRequest) {
        return Future.succeededFuture(userLoadedAuthorizations.authorizes(operationAuthorizationRequest));
    }
}
