package naga.platform.services.auth.spi;

import naga.commons.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UserMixin extends User {

    User getUser();

    default Future<Boolean> isAuthorized(Object authority) {
        return getUser().isAuthorized(authority);
    }
}
