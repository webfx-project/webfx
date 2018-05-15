package naga.platform.services.query.push.spi.impl;

import naga.platform.services.log.Logger;
import naga.platform.services.push.server.PushServerService;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResult;
import naga.platform.services.query.QueryService;
import naga.platform.services.query.push.PulseArgument;
import naga.platform.services.query.push.QueryPushArgument;
import naga.platform.services.query.push.QueryPushResult;
import naga.platform.services.query.push.QueryPushService;
import naga.platform.services.query.push.diff.QueryResultComparator;
import naga.platform.services.query.push.diff.QueryResultDiff;
import naga.platform.services.query.push.spi.QueryPushServiceProvider;
import naga.util.async.Future;

import java.util.ArrayList;
import java.util.Collection;
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

        boolean isActive() {
            return active != null && active;
        }

        void markAsResend() {
            // To resend the whole query result, we can just move this stream to the blank ones (as new clients always receive the whole result on first push)
            queryInfo.removeStreamInfo(this); // This will remove it from the filled streams list
            queryInfo.addStreamInfo(this); // This will add it to the blank streams list
        }
    }

    public static class QueryInfo {
        final QueryArgument queryArgument;
        private final List<StreamInfo> blankStreamInfos = new ArrayList<>(); // Contains new client streams that haven't received any result yet
        private final List<StreamInfo> filledStreamInfos = new ArrayList<>(); // Contains client streams that already received at least 1 result
        int activeStreamCount;
        private long lastQueryExecutionTime;
        private long lastPossibleChangeTime;
        private QueryResult lastQueryResult;

        QueryInfo(QueryArgument queryArgument) {
            this.queryArgument = queryArgument;
        }

        void touchExecuted() {
            lastQueryExecutionTime = now();
            lastPossibleChangeTime = 0;
        }

        boolean hasQueryResultChanged(QueryResult newQueryResult) {
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

        void markBlankStreamsAsFilled() {
            synchronized (this) {
                // Moving the blank streams into the filled ones
                filledStreamInfos.addAll(blankStreamInfos);
                blankStreamInfos.clear();
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
            Future<QueryResult> resultFuture;
            // Immediately reusing the last result if present and the query is not marked as dirty
            if (queryInfo.lastQueryResult != null && !queryInfo.isDirty())
                resultFuture = Future.succeededFuture(queryInfo.lastQueryResult);
            else { // Otherwise asking the query service to execute the query
                executedQueries++;
                queryInfo.touchExecuted();
                resultFuture = QueryService.executeQuery(queryInfo.queryArgument);
            }
            // Calling the pushResultToRelevantClients() method when the result is ready
            return resultFuture.map(queryResult -> {
                pushResultToRelevantClients(queryInfo, queryResult);
                return null;
            });
        }

        void pushResultToRelevantClients(QueryInfo queryInfo, QueryResult queryResult) {
            // Retrieving the last result
            QueryResult lastQueryResult = queryInfo.lastQueryResult;
            // Checking if there are changes compared to the last result - false if there are no "old" clients (that have already received the last result)
            boolean hasChanged = !queryInfo.filledStreamInfos.isEmpty() && queryInfo.hasQueryResultChanged(queryResult);
            if (hasChanged) // Increasing the changed queries counter in this case
                changedQueries++;
            // Setting the version number (same if no change or +1 if changed)
            if (lastQueryResult != null)
                queryResult.setVersionNumber(lastQueryResult.getVersionNumber() + (hasChanged ? 1 : 0));
            // Computing the diff in case of changes
            QueryResultDiff queryResultDiff = hasChanged ? QueryResultComparator.computeDiff(lastQueryResult, queryResult) : null;
            // Sending the whole query result to new clients (the blank streams that haven't received any result yet)
            pushResultToClients(queryInfo.blankStreamInfos, queryResult, null);
            // Sending only the diff to old clients (or sending the whole result if the comparator couldn't compute a diff)
            if (hasChanged)
                pushResultToClients(queryInfo.filledStreamInfos, queryResult, queryResultDiff);
            // Finally moving new clients to old clients
            queryInfo.markBlankStreamsAsFilled();
        }

        void pushResultToClients(Collection<StreamInfo> streamInfos, QueryResult queryResult, QueryResultDiff queryResultDiff) {
            for (StreamInfo streamInfo : new ArrayList<>(streamInfos)) { // Iterating on a copy to avoid concurrent modification exceptions
                pushedToClients++;
                QueryPushService.pushQueryResultToClient(new QueryPushResult(streamInfo.queryStreamId, queryResult, queryResultDiff), streamInfo.pushClientId)
                    .setHandler(ar -> {
                        if (ar.failed()) { // Handling push call failure
                            pushedFailed++;
                            removeStream(streamInfo);
                        }
                    });
            }
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
            StringBuilder sb = new StringBuilder(finishedStringStart());
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

        String finishedStringStart() {
            return "Pulse finished in " + (now() - startTime) + "ms - executed queries: " + executedQueries;
        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }

}
