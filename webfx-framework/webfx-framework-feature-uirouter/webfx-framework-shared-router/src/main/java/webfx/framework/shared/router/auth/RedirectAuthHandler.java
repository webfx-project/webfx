package webfx.framework.shared.router.auth;

import webfx.framework.shared.router.RoutingContext;
import webfx.framework.shared.router.auth.impl.RedirectAuthHandlerImpl;
import webfx.platform.shared.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface RedirectAuthHandler extends Handler<RoutingContext> {

    static RedirectAuthHandler create(String loginPath, String unauthorizedPath) {
        return new RedirectAuthHandlerImpl(loginPath, unauthorizedPath);
    }
}
