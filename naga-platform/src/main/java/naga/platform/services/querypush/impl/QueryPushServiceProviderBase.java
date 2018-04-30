package naga.platform.services.querypush.impl;

import naga.platform.services.log.spi.Logger;
import naga.platform.services.push.server.spi.PushServerService;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.spi.QueryService;
import naga.platform.services.querypush.PulseArgument;
import naga.platform.services.querypush.QueryPushArgument;
import naga.platform.services.querypush.QueryPushResult;
import naga.platform.services.querypush.spi.QueryPushServiceProvider;
import naga.platform.spi.Platform;
import naga.util.async.Future;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class QueryPushServiceProviderBase implements QueryPushServiceProvider {

    private final Map<Object, StreamInfo> streamInfos = new HashMap<>();
    private static int queryStreamIdSeq;

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
    public Future<Integer> executePulse(PulseArgument argument) {
        Future<Integer> future = Future.future();
        Collection<StreamInfo> collection = streamInfos.values();
        iteratePulse(collection.iterator(), collection.size(), System.currentTimeMillis(), future);
        return future;
    }

    private Future<Object> openStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        pushQueryResult(streamInfo);
        return Future.succeededFuture(streamInfo.queryStreamId);
    }

    private Future<Object> updateStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        streamInfo.queryArgument = argument.getQueryArgument();
        pushQueryResult(streamInfo);
        return Future.succeededFuture(streamInfo.queryStreamId);
    }

    private Future<Object> activateStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        streamInfo.active = argument.getActive();
        return Future.succeededFuture(streamInfo.queryStreamId);
    }

    private Future<Object> closeStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        streamInfo.close = argument.getClose();
        return Future.succeededFuture(streamInfo.queryStreamId);
    }

    private StreamInfo getStreamInfo(QueryPushArgument argument) {
        Object queryStreamId = argument.getQueryStreamId();
        if (queryStreamId != null)
            return streamInfos.get(queryStreamId);
        StreamInfo streamInfo = new StreamInfo(argument);
        streamInfos.put(streamInfo.queryStreamId = queryStreamIdSeq++, streamInfo);
        return streamInfo;
    }

    private void iteratePulse(Iterator<StreamInfo> it, int size, long startTime, Future<Integer> future) {
        if (!it.hasNext()) {
            Logger.log("Pulse finished (" + size +" streams in " + (System.currentTimeMillis() - startTime) + "ms)");
            future.complete(size);
        } else
            pushQueryResult(it.next()).setHandler(ar -> iteratePulse(it, size, startTime, future));
    }

    private Future<QueryPushResult> pushQueryResult(StreamInfo streamInfo) {
        return QueryService.executeQuery(streamInfo.queryArgument).map(rs -> {
            QueryPushResult queryPushResult = new QueryPushResult(streamInfo.queryStreamId, rs);
            PushServerService.callClientService("QueryPushResultClientListener", queryPushResult, Platform.bus(), streamInfo.pushClientId)
            .setHandler(ar -> {
                if (ar.failed())
                    streamInfos.remove(streamInfo.queryStreamId);
            });
            return queryPushResult;
        });
    }

    private static class StreamInfo {
        private Object queryStreamId;
        private Object pushClientId;
        private QueryArgument queryArgument;
        private Object dataSourceId;
        private Boolean active;
        private Boolean close;

        StreamInfo(QueryPushArgument arg) {
            queryStreamId = arg.getQueryStreamId();
            pushClientId = arg.getPushClientId();
            queryArgument = arg.getQueryArgument();
            dataSourceId = arg.getDataSourceId();
            active = arg.getActive();
            close = arg.getClose();
        }
    }
}
