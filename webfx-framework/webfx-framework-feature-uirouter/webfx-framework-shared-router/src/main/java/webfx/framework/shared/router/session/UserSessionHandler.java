package webfx.framework.shared.router.session;

import webfx.framework.shared.router.session.impl.UserSessionHandlerImpl;
import webfx.platform.shared.util.async.Handler;
import webfx.framework.shared.router.RoutingContext;

/**
 * @author Bruno Salmon
 */
public interface UserSessionHandler extends Handler<RoutingContext> {

    static UserSessionHandler create() {
        return new UserSessionHandlerImpl();
    }
}
