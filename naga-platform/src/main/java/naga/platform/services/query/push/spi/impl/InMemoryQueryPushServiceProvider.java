package naga.platform.services.query.push.spi.impl;

import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.push.PulseArgument;
import naga.platform.services.query.push.QueryPushArgument;
import naga.util.Objects;
import naga.util.async.Future;
import naga.util.collection.Collections;

import java.util.HashMap;
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
        return updateStream(argument);
    }

    @Override
    protected Future<Object> updateStream(QueryPushArgument argument) {
        StreamInfo streamInfo = getStreamInfo(argument);
        if (streamInfo == null)
            return Future.failedFuture(new IllegalArgumentException("Stream update request with unregistered queryStreamId=" + argument.getQueryStreamId()));
        QueryArgument queryArgument = argument.getQueryArgument();
        if (queryArgument != null)
            setStreamQueryArgument(streamInfo, queryArgument);
        Boolean active = argument.getActive();
        if (active != null)
            streamInfo.setActive(active);
        if (streamInfo.isActive())
            requestPulse(new PulseArgument(streamInfo.queryInfo));
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
    protected void setStreamQueryArgument(StreamInfo streamInfo, QueryArgument queryArgument) {
        QueryInfo queryInfo = streamInfo.queryInfo;
        if (queryInfo != null) {
            if (Objects.areEquals(queryArgument, queryInfo.queryArgument))
                return;
            queryInfo.removeStreamInfo(streamInfo);
        }
        queryInfo = queryInfos.get(queryArgument);
        if (queryInfo == null)
            queryInfos.put(queryArgument, queryInfo = new QueryInfo(queryArgument));
        queryInfo.addStreamInfo(streamInfo);
        streamInfo.queryInfo = queryInfo;
    }

    @Override
    protected void removeStream(StreamInfo streamInfo) {
        streamInfos.remove(streamInfo.queryStreamId);
        QueryInfo queryInfo = streamInfo.queryInfo;
        if (queryInfo != null) {
            queryInfo.removeStreamInfo(streamInfo);
            if (queryInfo.hasNoMoreStreams())
                queryInfos.remove(queryInfo.queryArgument);
        }
    }

    @Override
    protected void removePushClientStreams(Object pushClientId) {
        Collections.forEach(Collections.filter(streamInfos.values(), si -> Objects.areEquals(si.pushClientId, pushClientId)), this::removeStream);
    }

    PulsePass createPulsePass(PulseArgument argument) {
        return new InMemoryPulsePass(argument);
    }

    class InMemoryPulsePass extends PulsePass {

        InMemoryPulsePass(PulseArgument argument) {
            super(argument);
        }

        @Override
        void applyPulseArgument(PulseArgument argument) {
            if (argument.getQueryInfo() != null)
                argument.getQueryInfo().markAsDirty();
            else {
                Object dataSourceId = argument.getDataSourceId();
                for (QueryInfo queryInfo : queryInfos.values())
                    if (Objects.areEquals(queryInfo.queryArgument.getDataSourceId(), dataSourceId))
                        queryInfo.markAsDirty();
            }
            nextMostUrgentQueryNotYetExecuted = null;
        }

        @Override
        QueryInfo fetchNextMostUrgentQuery() {
            QueryInfo mostUrgentQuery = null;
            for (QueryInfo queryInfo : queryInfos.values())
                mostUrgentQuery = mostUrgentQuery(queryInfo, mostUrgentQuery);
            return mostUrgentQuery;
        }

        QueryInfo mostUrgentQuery(QueryInfo q1, QueryInfo q2) {
            if (q1.isDirty() && q1.activeStreamCount > 0 && (
                    q2 == null
                    || q1.getBlankStreamsCount() > q2.getBlankStreamsCount()
                    || q1.dirtyTime() > q2.dirtyTime()
                ))
                return q1;
            return q2;
        }
    }
}
