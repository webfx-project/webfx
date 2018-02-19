package naga.framework.router.auth.authn;

import naga.framework.router.impl.RedirectAuthHandlerImpl;

/**
 * @author Bruno Salmon
 */
public interface RedirectAuthHandler extends AuthHandler {

    static RedirectAuthHandler create(String loginPath, String unauthorizedPath) {
        return new RedirectAuthHandlerImpl(loginPath, unauthorizedPath);
    }
}
