package webfx.framework.router.auth;

import webfx.framework.router.RoutingContext;
import webfx.framework.router.auth.impl.RedirectAuthHandlerImpl;
import webfx.platform.shared.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface RedirectAuthHandler extends Handler<RoutingContext> {

    static RedirectAuthHandler create(String loginPath, String unauthorizedPath) {
        return new RedirectAuthHandlerImpl(loginPath, unauthorizedPath);
    }
}
