package webfx.platform.services.query.push.spi;

import webfx.platform.services.query.push.PulseArgument;
import webfx.platform.services.query.push.QueryPushArgument;
import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface QueryPushServiceProvider {

    Future<Object> executeQueryPush(QueryPushArgument argument);

    void requestPulse(PulseArgument argument);

}
