package naga.platform.services.querypush.spi.impl;

import naga.platform.services.log.Logger;
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
public class QueryPushServiceProviderBase implements QueryPushServiceProvider {

    private static int queryStreamIdSeq;
    private final Map<Object /* queryStreamId */, StreamInfo> streamInfos = new HashMap<>();
    private final Map<QueryArgument, QueryInfo> queryInfos = new HashMap<>();

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
        Collection<QueryInfo> collection = queryInfos.values();
        iteratePulse(collection.iterator(), collection.size(), System.currentTimeMillis(), future);
        return future;
    }

    private Future<Object> openStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        pushQueryResult(streamInfo.queryInfo);
        return Future.succeededFuture(streamInfo.queryStreamId);
    }

    private Future<Object> updateStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        streamInfo.setQueryArgument(argument.getQueryArgument());
        pushQueryResult(streamInfo.queryInfo);
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

    private void iteratePulse(Iterator<QueryInfo> it, int size, long startTime, Future<Integer> future) {
        if (!it.hasNext()) {
            Logger.log("Pulse finished (" + size +" streams in " + (System.currentTimeMillis() - startTime) + "ms)");
            future.complete(size);
        } else
            pushQueryResult(it.next()).setHandler(ar -> iteratePulse(it, size, startTime, future));
    }

    private Future<Void> pushQueryResult(QueryInfo queryInfo) {
        return QueryService.executeQuery(queryInfo.queryArgument).map(rs -> {
            for (StreamInfo streamInfo : new ArrayList<>(queryInfo.streamInfos)) {
                QueryPushResult queryPushResult = new QueryPushResult(streamInfo.queryStreamId, rs);
                PushServerService.callClientService("QueryPushResultClientListener", queryPushResult, Platform.bus(), streamInfo.pushClientId)
                    .setHandler(ar -> {
                        if (ar.failed())
                            streamInfo.remove();
                    });
            }
            return null;
        });
    }

    private class StreamInfo {
        private Object queryStreamId;
        private Object pushClientId;
        private Boolean active;
        private Boolean close;
        private QueryInfo queryInfo;

        StreamInfo(QueryPushArgument arg) {
            queryStreamId = arg.getQueryStreamId();
            pushClientId = arg.getPushClientId();
            active = arg.getActive();
            close = arg.getClose();
            setQueryArgument(arg.getQueryArgument());
        }

        void setQueryArgument(QueryArgument queryArgument) {
            if (queryInfo != null)
                queryInfo.streamInfos.remove(this);
            queryInfo = queryInfos.get(queryArgument);
            if (queryInfo == null)
                queryInfos.put(queryArgument, queryInfo = new QueryInfo(queryArgument));
            queryInfo.streamInfos.add(this);
        }

        void remove() {
            streamInfos.remove(queryStreamId);
            if (queryInfo != null) {
                queryInfo.streamInfos.remove(this);
                if (queryInfo.streamInfos.isEmpty())
                    queryInfos.remove(queryInfo.queryArgument);
            }
        }
    }

    private static class QueryInfo {
        private QueryArgument queryArgument;
        private List<StreamInfo> streamInfos = new ArrayList<>();

        QueryInfo(QueryArgument queryArgument) {
            this.queryArgument = queryArgument;
        }
    }
}
