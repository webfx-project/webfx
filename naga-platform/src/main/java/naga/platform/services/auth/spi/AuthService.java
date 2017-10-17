package naga.platform.services.auth.spi;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface AuthService {

    Future<User> authenticate(Object authInfo);

}
