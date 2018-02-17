package naga.framework.router.impl;

import naga.framework.router.RoutingContext;
import naga.platform.services.authz.User;

/**
 * @author Bruno Salmon
 */
public class UserHolder {
    RoutingContext context;
    User user;

    public UserHolder(RoutingContext context) {
        this.context = context;
    }

    public UserHolder(User user) {
        this.user = user;
    }
}
