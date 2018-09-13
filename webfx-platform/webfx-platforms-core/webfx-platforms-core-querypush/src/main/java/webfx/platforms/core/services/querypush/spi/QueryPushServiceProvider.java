package webfx.platforms.core.services.querypush.spi;

import webfx.platforms.core.services.querypush.PulseArgument;
import webfx.platforms.core.services.querypush.QueryPushArgument;
import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface QueryPushServiceProvider {

    Future<Object> executeQueryPush(QueryPushArgument argument);

    void requestPulse(PulseArgument argument);

}
