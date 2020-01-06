package webfx.framework.server.services.querypush.spi.impl.simple;

import webfx.framework.server.services.querypush.spi.impl.ServerQueryPushServiceProviderBase;
import webfx.framework.shared.services.querypush.PulseArgument;
import webfx.framework.shared.services.querypush.QueryPushArgument;
import webfx.platform.shared.datascope.DataScope;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.util.Objects;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.collection.Collections;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class SimpleInMemoryServerQueryPushServiceProvider extends ServerQueryPushServiceProviderBase {
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
        Boolean resend = argument.getResend();
        if (resend != null && resend)
            streamInfo.markAsResend();
        if (streamInfo.isActive()) {
            refreshActiveTree(streamInfo, active != null);
            executePulse(null); // Forcing a pulse cycle if none is running to refresh this query
        }
        return Future.succeededFuture(streamInfo.queryStreamId);
    }

    private void refreshActiveTree(StreamInfo streamInfo, boolean includeActiveChildren) {
        streamInfo.queryInfo.markAsReactivated();
        if (includeActiveChildren)
            streamInfo.childrenStreamInfos.forEach(csi -> {
                System.out.println(csi);
                if (csi.active)
                    refreshActiveTree(csi, true);
            });
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
    protected StreamInfo getStreamInfo(Object queryStreamId) {
        return streamInfos.get(queryStreamId);
    }

    @Override
    protected void setStreamQueryArgument(StreamInfo streamInfo, QueryArgument queryArgument) {
        // Getting the previous queryInfo associated with this stream info
        QueryInfo previousQueryInfo = streamInfo.queryInfo;
        if (previousQueryInfo != null) { // If set
            // and has the same query argument
            if (Objects.areEquals(queryArgument, previousQueryInfo.getQueryArgument()))
                return; // No change is needed
            // otherwise (ie the query argument has changed), this streamInfo shouldn't be associated with that previous queryInfo anymore
            removeStreamFromQueryInfo(streamInfo, previousQueryInfo);
        }
        // Getting the requested query info (may already exist if associated with some other streams)
        QueryInfo requestedQueryInfo = queryInfos.get(queryArgument);
        if (requestedQueryInfo == null) { // creating it (and register it) if it doesn't exist
            // If the queryString was null (network optimization when it's the same as previous),
            if (queryArgument.getStatement() == null && previousQueryInfo != null)
                // we reconstitute the complete argument reusing the previous queryString
                queryArgument = QueryArgument.builder().copy(queryArgument).setStatement(previousQueryInfo.getQueryArgument().getStatement()).build();
            queryInfos.put(queryArgument, requestedQueryInfo = new QueryInfo(queryArgument));
        }
        // Associating this streamInfo to this requested queryInfo
        requestedQueryInfo.addStreamInfo(streamInfo);
        streamInfo.queryInfo = requestedQueryInfo;
    }

    @Override
    protected void removeStream(StreamInfo streamInfo) {
        streamInfos.remove(streamInfo.queryStreamId);
        removeStreamFromQueryInfo(streamInfo, streamInfo.queryInfo);
    }

    private void removeStreamFromQueryInfo(StreamInfo streamInfo, QueryInfo queryInfo) {
        if (queryInfo != null) {
            queryInfo.removeStreamInfo(streamInfo);
            if (queryInfo.hasNoMoreStreams())
                queryInfos.remove(queryInfo.getQueryArgument());
        }
    }

    @Override
    protected void removePushClientStreams(Object pushClientId) {
        Collections.forEach(Collections.filter(streamInfos.values(), si -> Objects.areEquals(si.pushClientId, pushClientId)), this::removeStream);
    }

    protected PulsePass createPulsePass(PulseArgument argument) {
        return new InMemoryPulsePass(argument);
    }

    final class InMemoryPulsePass extends PulsePass {

        InMemoryPulsePass(PulseArgument argument) {
            super(argument);
        }

        /**
         * This method should mark as dirty all queries (through queryInfo) that are impacted by the modification
         * reported by the pulse argument.
         *
         * @param argument the pulse argument
         */
        @Override
        protected void applyPulseArgument(PulseArgument argument) {
            if (argument != null) { // argument may be null (some code can wake up pulse by calling executePulse(null))
                // We need to mark dirty all queries impacted by the modification
                //System.out.println("applyPulseArgument(" + argument + ")");
                Object dataSourceId = argument.getDataSourceId();
                for (QueryInfo queryInfo : queryInfos.values()) {
                    // First criteria: must be of the same data source
                    if (!Objects.areEquals(dataSourceId, queryInfo.getQueryArgument().getDataSourceId()))
                        continue; // Avoiding an unnecessary costly query check! :-)
                    // Second criteria: the update scope must impact the query scope (ex: modify a field that the query reads)
                    DataScope modifiedScope = argument.getDataScope();
                    if (modifiedScope != null) {
                        DataScope queryScope = queryInfo.getQueryScope();
                        if (queryScope != null && !modifiedScope.intersects(queryScope)) {
                            //System.out.println("Skipped non intersecting scopes " + modifiedScope.getClass().getSimpleName() + " and " + queryScope.getClass().getSimpleName());
                            continue; // Avoiding an unnecessary costly query check! :-)
                        }
                    }
                    //System.out.println("Marked query info as dirty: " + queryInfo.getQueryArgument().getStatement());
                    queryInfo.markAsDirty();
                    nextMostUrgentQueryNotYetRefreshed = null; // To force a re-computation of the most urgent query to refresh
                }
            }
        }

        @Override
        protected QueryInfo fetchNextMostUrgentQuery() {
            QueryInfo mostUrgentQuery = null;
            for (QueryInfo queryInfo : queryInfos.values())
                mostUrgentQuery = mostUrgentQuery(queryInfo, mostUrgentQuery);
            return mostUrgentQuery;
        }

        QueryInfo mostUrgentQuery(QueryInfo q1, QueryInfo q2) {
            if (q1.needsRefresh() && (
                    q2 == null
                            || q1.getActiveNewStreamCount() > q2.getActiveNewStreamCount()
                            || q1.dirtyTime() > q2.dirtyTime()
            ))
                return q1;
            return q2;
        }

        @Override
        protected String finishedStringStart() {
            return super.finishedStringStart() + "(/" + queryInfos.size() + ")";
        }
    }
}
