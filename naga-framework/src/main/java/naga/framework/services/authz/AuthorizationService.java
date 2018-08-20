package naga.framework.services.authz;

import naga.framework.services.authz.spi.AuthorizationServiceProvider;
import naga.util.async.Future;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public final class AuthorizationService {

    public static Future<Boolean> isAuthorized(Object operationAuthorizationRequest, Object userPrincipal) {
        return getProvider().isAuthorized(operationAuthorizationRequest, userPrincipal);
    }

    public static AuthorizationServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(AuthorizationServiceProvider.class);
    }
}
