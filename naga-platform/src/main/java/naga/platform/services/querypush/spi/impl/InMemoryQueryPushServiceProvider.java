package naga.platform.services.querypush.spi.impl;

import naga.platform.services.query.QueryArgument;
import naga.platform.services.querypush.PulseArgument;
import naga.platform.services.querypush.QueryPushArgument;
import naga.util.Objects;
import naga.util.async.Future;

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

    private synchronized StreamInfo getStreamInfo(QueryPushArgument argument) {
        Object queryStreamId = argument.getQueryStreamId();
        if (queryStreamId != null)
            return streamInfos.get(queryStreamId);
        StreamInfo streamInfo = new StreamInfo(argument);
        streamInfos.put(streamInfo.queryStreamId = queryStreamIdSeq++, streamInfo);
        return streamInfo;
    }

    @Override
    protected synchronized void setStreamQueryArgument(StreamInfo streamInfo, QueryArgument queryArgument) {
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
            if (queryInfo.streamInfos.isEmpty())
                queryInfos.remove(queryInfo.queryArgument);
        }
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
            nextHottestQueryNotYetReturned = null;
        }

        @Override
        QueryInfo fetchNextHottestQuery() {
            QueryInfo hottestQuery = null;
            for (QueryInfo queryInfo : queryInfos.values())
                hottestQuery = hottestQuery(queryInfo, hottestQuery);
            return hottestQuery;
        }

        QueryInfo hottestQuery(QueryInfo q1, QueryInfo q2) {
            if (!q1.isDirty() || q1.activeStreamCount == 0)
                return q2;
            if (q2 == null)
                return q1;
            return q1.dirtyTime() > q2.dirtyTime() ? q1 : q2;
        }
    }
}
