package naga.framework.spi.authn;

import naga.util.async.Future;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class AuthenticationService {

    public static Future<?> authenticate(Object userCredentials) {
        return getProvider().authenticate(userCredentials);
    }

    public static AuthenticationServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(AuthenticationServiceProvider.class);
    }
}
