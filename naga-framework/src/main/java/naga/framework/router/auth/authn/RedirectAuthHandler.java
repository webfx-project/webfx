package naga.framework.router.auth.authn;

import naga.framework.router.impl.RedirectAuthHandlerImpl;
import naga.platform.services.auth.spi.authn.AuthService;

/**
 * @author Bruno Salmon
 */
public interface RedirectAuthHandler extends AuthHandler {

    static RedirectAuthHandler create(AuthService authService, String loginPath, String unauthorizedPath) {
        return new RedirectAuthHandlerImpl(authService, loginPath, unauthorizedPath);
    }
}
