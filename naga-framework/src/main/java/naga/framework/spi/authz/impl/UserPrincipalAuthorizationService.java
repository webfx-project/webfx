package naga.framework.spi.authz.impl;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UserPrincipalAuthorizationService {

    Object getUserPrincipal();

    Future<Boolean> isAuthorized(Object operationAuthorizationRequest);

}
