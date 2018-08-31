package webfx.framework.services.authn.spi;

import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface AuthenticationServiceProvider {

    Future<?> authenticate(Object userCredentials);

}
