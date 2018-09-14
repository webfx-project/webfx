package webfx.platforms.core.services.query.spi.impl;

import webfx.platforms.core.services.buscall.BusCallService;
import webfx.platforms.core.services.query.QueryArgument;
import webfx.platforms.core.services.query.QueryService;
import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class LocalOrRemoteQueryServiceProviderImpl extends LocalQueryServiceProviderImpl {

    protected <T> Future<T> executeRemoteQuery(QueryArgument argument) {
        return BusCallService.call(QueryService.QUERY_SERVICE_ADDRESS, argument);
    }
}
