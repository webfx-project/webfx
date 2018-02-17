package naga.framework.router.auth.authn;

import naga.framework.router.impl.RedirectAuthHandlerImpl;
import naga.platform.services.authn.AuthenticationService;

/**
 * @author Bruno Salmon
 */
public interface RedirectAuthHandler extends AuthHandler {

    static RedirectAuthHandler create(AuthenticationService authenticationService, String loginPath, String unauthorizedPath) {
        return new RedirectAuthHandlerImpl(authenticationService, loginPath, unauthorizedPath);
    }
}
