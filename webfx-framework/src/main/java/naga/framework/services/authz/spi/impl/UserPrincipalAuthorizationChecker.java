package naga.framework.services.authz.spi.impl;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UserPrincipalAuthorizationChecker {

    Object getUserPrincipal();

    Future<Boolean> isAuthorized(Object operationAuthorizationRequest);

}
