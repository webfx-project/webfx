package naga.platform.services.auth.spi.authn;

import naga.platform.services.auth.spi.authz.User;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface AuthService {

    Future<User> authenticate(Object authnInfo);

}
