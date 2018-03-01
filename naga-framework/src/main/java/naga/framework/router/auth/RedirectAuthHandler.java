package naga.framework.router.auth;

import naga.framework.router.RoutingContext;
import naga.framework.router.impl.RedirectAuthHandlerImpl;
import naga.util.async.Handler;

/**
 * @author Bruno Salmon
 */
public interface RedirectAuthHandler extends Handler<RoutingContext> {

    static RedirectAuthHandler create(String loginPath, String unauthorizedPath) {
        return new RedirectAuthHandlerImpl(loginPath, unauthorizedPath);
    }
}
