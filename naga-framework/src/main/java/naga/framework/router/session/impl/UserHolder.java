package naga.framework.router.session.impl;

import naga.framework.router.RoutingContext;

/**
 * @author Bruno Salmon
 */
public class UserHolder {
    RoutingContext context;
    Object userPrincipal;

    public UserHolder(RoutingContext context) {
        this.context = context;
    }

    public UserHolder(Object userPrincipal) {
        this.userPrincipal = userPrincipal;
    }
}
