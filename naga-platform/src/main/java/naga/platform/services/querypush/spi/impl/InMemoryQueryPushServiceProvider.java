package naga.platform.services.querypush.spi.impl;

import naga.platform.services.log.Logger;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.querypush.PulseArgument;
import naga.platform.services.querypush.QueryPushArgument;
import naga.util.async.Future;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class InMemoryQueryPushServiceProvider extends QueryPushServiceProviderBase {
    private static int queryStreamIdSeq;
    private final Map<Object /* queryStreamId */, StreamInfo> streamInfos = new HashMap<>();
    private final Map<QueryArgument, QueryInfo> queryInfos = new HashMap<>();

    @Override
    protected Future<Object> openStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        pushQueryResult(streamInfo.queryInfo);
        return Future.succeededFuture(streamInfo.queryStreamId);
    }

    @Override
    protected Future<Object> updateStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        setStreamQueryArgument(streamInfo, argument.getQueryArgument());
        pushQueryResult(streamInfo.queryInfo);
        return Future.succeededFuture(streamInfo.queryStreamId);
    }

    @Override
    protected Future<Object> activateStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        streamInfo.active = argument.getActive();
        return Future.succeededFuture(streamInfo.queryStreamId);
    }

    @Override
    protected Future<Object> closeStream(QueryPushArgument argument) {
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

    @Override
    public void requestPulse(PulseArgument argument) {
        Collection<QueryInfo> collection = queryInfos.values();
        iteratePulse(collection.iterator(), collection.size(), System.currentTimeMillis());
    }

    private void iteratePulse(Iterator<QueryInfo> it, int size, long startTime) {
        if (!it.hasNext())
            Logger.log("Pulse finished (" + size +" streams in " + (System.currentTimeMillis() - startTime) + "ms)");
        else
            pushQueryResult(it.next()).setHandler(ar -> iteratePulse(it, size, startTime));
    }

    @Override
    protected void setStreamQueryArgument(StreamInfo streamInfo, QueryArgument queryArgument) {
        QueryInfo queryInfo = streamInfo.queryInfo;
        if (queryInfo != null)
            queryInfo.streamInfos.remove(streamInfo);
        queryInfo = queryInfos.get(queryArgument);
        if (queryInfo == null)
            queryInfos.put(queryArgument, streamInfo.queryInfo = queryInfo = new QueryInfo(queryArgument));
        queryInfo.streamInfos.add(streamInfo);
    }

    @Override
    protected void removeStream(StreamInfo streamInfo) {
        streamInfos.remove(streamInfo.queryStreamId);
        QueryInfo queryInfo = streamInfo.queryInfo;
        if (queryInfo != null) {
            queryInfo.streamInfos.remove(streamInfo);
            if (queryInfo.streamInfos.isEmpty())
                queryInfos.remove(queryInfo.queryArgument);
        }
    }
}
