package naga.framework.spi.authz;

import naga.util.async.Future;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationService {

    Future<Boolean> isAuthorized(Object operationAuthorizationRequest, Object userPrincipal);

    static AuthorizationService get() {
        return ServiceLoaderHelper.loadService(AuthorizationService.class);
    }
}
