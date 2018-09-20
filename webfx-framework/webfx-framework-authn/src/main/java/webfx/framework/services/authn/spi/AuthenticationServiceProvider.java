package webfx.framework.services.authn.spi;

import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface AuthenticationServiceProvider {

    Future<?> authenticate(Object userCredentials);

}
