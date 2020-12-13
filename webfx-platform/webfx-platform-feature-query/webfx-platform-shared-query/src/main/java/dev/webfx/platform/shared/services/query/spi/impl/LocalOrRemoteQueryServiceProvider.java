package dev.webfx.platform.shared.services.query.spi.impl;

import dev.webfx.platform.shared.services.buscall.BusCallService;
import dev.webfx.platform.shared.util.async.Future;
import dev.webfx.platform.shared.services.query.QueryArgument;
import dev.webfx.platform.shared.services.query.QueryService;

/**
 * @author Bruno Salmon
 *
 * TODO Turn this service provider into a service interceptor (requires the build tool to provide the automatic conditional
 * TODO module inclusion (here: when both webfx-platform-shared-query and webfx-platform-shared-buscall are included)
 */
public class LocalOrRemoteQueryServiceProvider extends LocalQueryServiceProvider {

    protected <T> Future<T> executeRemoteQuery(QueryArgument argument) {
        return BusCallService.call(QueryService.QUERY_SERVICE_ADDRESS, argument);
    }
}
