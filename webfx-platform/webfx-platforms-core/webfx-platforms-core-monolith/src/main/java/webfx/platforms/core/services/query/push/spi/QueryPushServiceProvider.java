package webfx.platforms.core.services.query.push.spi;

import webfx.platforms.core.services.query.push.PulseArgument;
import webfx.platforms.core.services.query.push.QueryPushArgument;
import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface QueryPushServiceProvider {

    Future<Object> executeQueryPush(QueryPushArgument argument);

    void requestPulse(PulseArgument argument);

}
