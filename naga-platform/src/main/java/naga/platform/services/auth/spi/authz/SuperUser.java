package naga.platform.services.auth.spi.authz;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class SuperUser implements User {

    @Override
    public Future<Boolean> isAuthorized(Object operationAuthorizationRequest) {
        return Future.succeededFuture(true);
    }
}
