package naga.framework.router.impl;

import naga.framework.router.RoutingContext;
import naga.framework.router.handler.AuthHandler;

/**
 * @author Bruno Salmon
 */
public class AuthHandlerImpl implements AuthHandler {

    @Override
    public void handle(RoutingContext context) {
        context.next();
    }
}
