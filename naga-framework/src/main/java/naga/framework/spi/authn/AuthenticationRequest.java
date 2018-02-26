package naga.framework.spi.authn;

import naga.util.async.Future;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class AuthenticationRequest {

    private Object userCredentials;
    private AuthenticationServiceProvider provider;

    public Object getUserCredentials() {
        return userCredentials;
    }

    public AuthenticationRequest setUserCredentials(Object userCredentials) {
        this.userCredentials = userCredentials;
        return this;
    }

    public AuthenticationServiceProvider getProvider() {
        return provider;
    }

    public AuthenticationRequest setProvider(AuthenticationServiceProvider provider) {
        this.provider = provider;
        return this;
    }

    public AuthenticationRequest complete() {
        if (provider == null)
            setProvider(ServiceLoaderHelper.loadService(AuthenticationServiceProvider.class));
        return this;
    }

    public Future<?> executeAsync() {
        return complete().getProvider().authenticate(getUserCredentials());
    }

}
