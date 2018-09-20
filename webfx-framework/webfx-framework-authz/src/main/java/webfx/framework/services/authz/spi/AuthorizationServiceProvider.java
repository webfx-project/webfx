package webfx.framework.services.authz.spi;

import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationServiceProvider {

    Future<Boolean> isAuthorized(Object operationAuthorizationRequest, Object userPrincipal);

}
