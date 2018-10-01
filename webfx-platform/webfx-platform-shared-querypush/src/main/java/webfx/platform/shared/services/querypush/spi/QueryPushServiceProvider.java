package webfx.platform.shared.services.querypush.spi;

import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.services.querypush.PulseArgument;
import webfx.platform.shared.services.querypush.QueryPushArgument;

/**
 * @author Bruno Salmon
 */
public interface QueryPushServiceProvider {

    Future<Object> executeQueryPush(QueryPushArgument argument);

    void requestPulse(PulseArgument argument);

}
