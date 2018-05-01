package naga.platform.services.querypush.spi.impl;

import naga.platform.services.push.server.PushServerService;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryService;
import naga.platform.services.querypush.PulseArgument;
import naga.platform.services.querypush.QueryPushArgument;
import naga.platform.services.querypush.QueryPushResult;
import naga.platform.services.querypush.spi.QueryPushServiceProvider;
import naga.platform.spi.Platform;
import naga.util.async.Future;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public abstract class QueryPushServiceProviderBase implements QueryPushServiceProvider {

    @Override
    public Future<Object> executeQueryPush(QueryPushArgument argument) {
        // Dispatching to the correct method in dependence of the argument:
        if (argument.isOpenStreamArgument())
            return openStream(argument);
        if (argument.isUpdateStreamArgument())
            return updateStream(argument);
        if (argument.isActivateStreamArgument())
            return activateStream(argument);
        if (argument.isCloseStreamArgument())
            return closeStream(argument);
        return Future.failedFuture(new IllegalArgumentException());
    }

    @Override
    public abstract void requestPulse(PulseArgument argument);

    protected abstract Future<Object> openStream(QueryPushArgument argument);

    protected abstract Future<Object> updateStream(QueryPushArgument argument);

    protected abstract Future<Object> activateStream(QueryPushArgument argument);

    protected abstract Future<Object> closeStream(QueryPushArgument argument);

    Future<Void> pushQueryResult(QueryInfo queryInfo) {
        return QueryService.executeQuery(queryInfo.queryArgument).map(rs -> {
            for (StreamInfo streamInfo : new ArrayList<>(queryInfo.streamInfos)) {
                QueryPushResult queryPushResult = new QueryPushResult(streamInfo.queryStreamId, rs);
                PushServerService.callClientService("QueryPushResultClientListener", queryPushResult, Platform.bus(), streamInfo.pushClientId)
                    .setHandler(ar -> {
                        if (ar.failed())
                            removeStream(streamInfo);
                    });
            }
            return null;
        });
    }

    protected abstract void setStreamQueryArgument(StreamInfo streamInfo, QueryArgument queryArgument);

    protected abstract void removeStream(StreamInfo streamInfo);

    class StreamInfo {
        Object queryStreamId;
        final Object pushClientId;
        Boolean active;
        Boolean close;
        QueryInfo queryInfo;

        StreamInfo(QueryPushArgument arg) {
            queryStreamId = arg.getQueryStreamId();
            pushClientId = arg.getPushClientId();
            active = arg.getActive();
            close = arg.getClose();
            setStreamQueryArgument(this, arg.getQueryArgument());
        }
    }

    static class QueryInfo {
        final QueryArgument queryArgument;
        final List<StreamInfo> streamInfos = new ArrayList<>();

        QueryInfo(QueryArgument queryArgument) {
            this.queryArgument = queryArgument;
        }
    }
}
