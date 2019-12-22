package webfx.platform.shared.services.query.spi.impl;

import webfx.platform.shared.services.buscall.BusCallService;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryService;

/**
 * @author Bruno Salmon
 */
public class LocalOrRemoteQueryServiceProvider extends LocalQueryServiceProvider {

    protected <T> Future<T> executeRemoteQuery(QueryArgument argument) {
        return BusCallService.call(QueryService.QUERY_SERVICE_ADDRESS, argument);
    }
}
