package naga.platform.services.authn.spi;

import naga.util.async.Future;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public interface AuthenticationService {

    Future<?> authenticate(Object userCredentials);

    static AuthenticationService get() {
        return ServiceLoaderHelper.loadService(AuthenticationService.class);
    }

}
