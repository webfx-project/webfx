package webfx.framework.shared.services.authn.spi;

import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface AuthenticationServiceProvider {

    Future<?> authenticate(Object userCredentials);

}
