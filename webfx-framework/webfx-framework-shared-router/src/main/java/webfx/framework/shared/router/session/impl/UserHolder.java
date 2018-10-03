package webfx.framework.shared.router.session.impl;

import webfx.framework.shared.router.RoutingContext;

/**
 * @author Bruno Salmon
 */
public final class UserHolder {
    RoutingContext context;
    Object userPrincipal;

    public UserHolder(RoutingContext context) {
        this.context = context;
    }

    public UserHolder(Object userPrincipal) {
        this.userPrincipal = userPrincipal;
    }
}
