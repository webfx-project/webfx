package naga.platform.services.query.push.spi.impl;

import naga.platform.services.log.Logger;
import naga.platform.services.push.server.PushServerService;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.services.query.QueryService;
import naga.platform.services.query.push.PulseArgument;
import naga.platform.services.query.push.QueryPushArgument;
import naga.platform.services.query.push.QueryPushResult;
import naga.platform.services.query.push.QueryPushService;
import naga.platform.services.query.push.spi.QueryPushServiceProvider;
import naga.util.async.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public abstract class QueryPushServiceProviderBase implements QueryPushServiceProvider {
    private PulsePass pulsePass;

    protected QueryPushServiceProviderBase() {
        PushServerService.addPushClientDisconnectListener(this::removePushClientStreams);
    }

    @Override
    public Future<Object> executeQueryPush(QueryPushArgument argument) {
        // Dispatching to the correct method in dependence of the argument:
        if (argument.isOpenStreamArgument())
            return openStream(argument);
        if (argument.isUpdateStreamArgument())
            return updateStream(argument);
        if (argument.isCloseStreamArgument())
            return closeStream(argument);
        return Future.failedFuture(new IllegalArgumentException());
    }

    protected abstract Future<Object> openStream(QueryPushArgument argument);

    protected abstract Future<Object> updateStream(QueryPushArgument argument);

    protected abstract Future<Object> closeStream(QueryPushArgument argument);

    @Override
    public void requestPulse(PulseArgument argument) {
        if (pulsePass == null || pulsePass.isFinished())
            pulsePass = createPulsePass(argument);
        else
            pulsePass.applyPulseArgument(argument);
    }

    abstract PulsePass createPulsePass(PulseArgument argument);

    protected abstract void setStreamQueryArgument(StreamInfo streamInfo, QueryArgument queryArgument);

    protected abstract void removeStream(StreamInfo streamInfo);

    protected abstract void removePushClientStreams(Object pushClientId);

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

        void setActive(boolean active) {
            if (this.active != active) {
                this.active = active;
                queryInfo.updateActiveStreamCount(active);
            }
        }

        public boolean isActive() {
            return active != null && active;
        }
    }

    public static class QueryInfo {
        final QueryArgument queryArgument;
        private List<StreamInfo> blankStreamInfos = new ArrayList<>(); // Contains new client streams that haven't received any result yet
        private final List<StreamInfo> filledStreamInfos = new ArrayList<>(); // Contains client streams that already received at least 1 result
        int activeStreamCount;
        private long lastQueryExecutionTime;
        private long lastPossibleChangeTime;
        private QueryResultSet lastQueryResult;

        QueryInfo(QueryArgument queryArgument) {
            this.queryArgument = queryArgument;
        }

        void touchExecuted() {
            lastQueryExecutionTime = now();
            lastPossibleChangeTime = 0;
        }

        boolean hasQueryResultChanged(QueryResultSet newQueryResult) {
            if (Objects.equals(newQueryResult, lastQueryResult))
                return false;
            lastQueryResult = newQueryResult;
            return true;
        }

        void markAsDirty() {
            lastPossibleChangeTime = now();
        }

        boolean isDirty() {
            return dirtyTime() >= 0;
        }

        long dirtyTime() {
            return lastPossibleChangeTime - lastQueryExecutionTime;
        }

        void addStreamInfo(StreamInfo streamInfo) {
            synchronized (this) {
                blankStreamInfos.add(streamInfo);
                if (streamInfo.active)
                    activeStreamCount++;
            }
        }

        void removeStreamInfo(StreamInfo streamInfo) {
            synchronized (this) {
                if (filledStreamInfos.remove(streamInfo) || blankStreamInfos.remove(streamInfo)) {
                    if (streamInfo.active)
                        activeStreamCount--;
                };
            }
        }

        int getBlankStreamsCount() {
            return blankStreamInfos.size();
        }

        List<StreamInfo> markBlankStreamsAsFilled() {
            synchronized (this) {
                // Keeping a reference to the current blank streams list (will be returned) before clearing it
                List<StreamInfo> streamInfos = blankStreamInfos;
                // Moving the blank streams into the filled ones
                filledStreamInfos.addAll(streamInfos);
                blankStreamInfos = new ArrayList<>();
                // Returning the previous blank streams that are now marked as filled
                return streamInfos;
            }
        }

        void updateActiveStreamCount(boolean increment) {
            activeStreamCount += increment ? 1 : -1;
        }

        boolean hasNoMoreStreams() {
            return filledStreamInfos.isEmpty() && blankStreamInfos.isEmpty();
        }
    }

    abstract class PulsePass {
        final long startTime = now();
        int executedQueries, changedQueries, pushedToClients, pushedFailed;
        QueryInfo nextMostUrgentQueryNotYetExecuted;
        boolean finished;

        PulsePass(PulseArgument argument) {
            applyPulseArgument(argument);
            executeNextMostUrgentQueryIfAny();
        }

        abstract void applyPulseArgument(PulseArgument argument);

        void executeNextMostUrgentQueryIfAny() {
            if (isFinished())
                onFinished();
            else
                executeQueryAndPushResultToRelevantClients(getNextMostUrgentQuery()).setHandler(ar -> executeNextMostUrgentQueryIfAny());
        }

        Future<Void> executeQueryAndPushResultToRelevantClients(QueryInfo queryInfo) {
            executedQueries++;
            queryInfo.touchExecuted();
            return QueryService.executeQuery(queryInfo.queryArgument).map(queryResult -> {
                // Merging the new clients (the blank streams that haven't received any result yet) into the existing ones (those that already received at least 1 result)
                List<StreamInfo> relevantStreamInfos = queryInfo.markBlankStreamsAsFilled(); // The relevant clients are first set to these new clients (they are relevant if the result hasn't changed)
                if (queryInfo.hasQueryResultChanged(queryResult)) { // But if the result has changed, we need to push this result to all clients (and not only the new ones)
                    changedQueries++;
                    relevantStreamInfos = new ArrayList<>(queryInfo.filledStreamInfos); // all clients - making a safe copy to avoid concurrent modification exception when calling removeStream()
                }
                // Now pushing the result to all relevant clients
                for (StreamInfo streamInfo : relevantStreamInfos) {
                    pushedToClients++;
                    QueryPushService.pushQueryResultToClient(new QueryPushResult(streamInfo.queryStreamId, queryResult), streamInfo.pushClientId)
                        .setHandler(ar -> {
                            if (ar.failed()) { // Handling push call failure
                                pushedFailed++;
                                removeStream(streamInfo);
                            }
                        });
                }
                return null;
            });
        }

        QueryInfo getNextMostUrgentQuery() {
            QueryInfo nextMostUrgentQuery = getNextMostUrgentQueryNotYetExecuted();
            nextMostUrgentQueryNotYetExecuted = null;
            return nextMostUrgentQuery;
        }

        QueryInfo getNextMostUrgentQueryNotYetExecuted() {
            if (nextMostUrgentQueryNotYetExecuted == null)
                nextMostUrgentQueryNotYetExecuted = fetchNextMostUrgentQuery();
            return nextMostUrgentQueryNotYetExecuted;
        }

        abstract QueryInfo fetchNextMostUrgentQuery();

        boolean isFinished() {
            return finished |= getNextMostUrgentQueryNotYetExecuted() == null;
        }

        void onFinished() {
            StringBuilder sb = new StringBuilder("Pulse finished in " + (now() - startTime) + "ms - executed queries: " + executedQueries);
            if (executedQueries > 0) {
                sb.append(", changed: ").append(changedQueries);
                if (pushedToClients > 0) {
                    sb.append(", pushed: ").append(pushedToClients);
                    if (pushedFailed > 0)
                        sb.append(", failed: ").append(pushedFailed);
                }
            }
            Logger.log(sb);
        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }

}
