package webfx.framework.services.authz;

import webfx.framework.services.authz.spi.AuthorizationServiceProvider;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class AuthorizationService {

    public static Future<Boolean> isAuthorized(Object operationAuthorizationRequest, Object userPrincipal) {
        return getProvider().isAuthorized(operationAuthorizationRequest, userPrincipal);
    }

    public static AuthorizationServiceProvider getProvider() {
        return SingleServiceLoader.loadService(AuthorizationServiceProvider.class);
    }
}
