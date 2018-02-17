package naga.platform.services.authn;

import naga.platform.services.auth.spi.authz.User;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface AuthenticationService {

    Future<User> authenticate(Object userCredentials);

}
