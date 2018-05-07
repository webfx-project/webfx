package naga.platform.services.query.push.spi;

import naga.platform.services.query.push.PulseArgument;
import naga.platform.services.query.push.QueryPushArgument;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface QueryPushServiceProvider {

    Future<Object> executeQueryPush(QueryPushArgument argument);

    void requestPulse(PulseArgument argument);

}
