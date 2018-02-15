package naga.platform.services.auth.spi.authz;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class LoadedAuthorizationsUser implements User {

    private final Authorization userLoadedAuthorizations;

    public LoadedAuthorizationsUser(Authorization userLoadedAuthorizations) {
        this.userLoadedAuthorizations = userLoadedAuthorizations;
    }

    @Override
    public Future<Boolean> isAuthorized(Object authority) {
        return Future.succeededFuture(userLoadedAuthorizations.authorizes(authority));
    }
}
