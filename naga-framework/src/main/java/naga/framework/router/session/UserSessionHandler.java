package naga.framework.router.session;

import naga.framework.router.impl.UserSessionHandlerImpl;
import naga.util.async.Handler;
import naga.framework.router.RoutingContext;

/**
 * @author Bruno Salmon
 */
public interface UserSessionHandler extends Handler<RoutingContext> {

    static UserSessionHandler create() {
        return new UserSessionHandlerImpl();
    }
}
