package naga.framework.router.handler;

import naga.commons.util.async.Handler;
import naga.framework.router.RoutingContext;
import naga.framework.router.impl.UserSessionHandlerImpl;

/**
 * @author Bruno Salmon
 */
public interface UserSessionHandler extends Handler<RoutingContext> {

    static UserSessionHandler create() {
        return new UserSessionHandlerImpl();
    }
}
