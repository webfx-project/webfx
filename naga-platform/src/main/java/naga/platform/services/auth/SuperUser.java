package naga.platform.services.auth;

import naga.commons.util.async.Future;
import naga.platform.services.auth.spi.User;

/**
 * @author Bruno Salmon
 */
public class SuperUser implements User {

    @Override
    public Future<Boolean> isAuthorized(Object authority) {
        return Future.succeededFuture(true);
    }
}
