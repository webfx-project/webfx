package webfx.framework.services.authn;

import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public final class AuthenticationRequest {

    private Object userCredentials;

    public Object getUserCredentials() {
        return userCredentials;
    }

    public AuthenticationRequest setUserCredentials(Object userCredentials) {
        this.userCredentials = userCredentials;
        return this;
    }

    public Future<?> executeAsync() {
        return AuthenticationService.authenticate(getUserCredentials());
    }

}