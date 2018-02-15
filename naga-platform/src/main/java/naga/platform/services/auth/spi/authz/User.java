package naga.platform.services.auth.spi.authz;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface User {

    Future<Boolean> isAuthorized(Object authority);

}
