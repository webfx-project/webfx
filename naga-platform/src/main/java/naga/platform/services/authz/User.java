package naga.platform.services.authz;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface User {

    Future<Boolean> isAuthorized(Object operationAuthorizationRequest);

}
