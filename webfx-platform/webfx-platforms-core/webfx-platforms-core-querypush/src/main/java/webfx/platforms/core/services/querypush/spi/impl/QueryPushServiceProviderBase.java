package webfx.platforms.core.services.querypush.spi.impl;

import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.push.server.PushServerService;
import webfx.platforms.core.services.query.QueryArgument;
import webfx.platforms.core.services.query.QueryResult;
import webfx.platforms.core.services.query.QueryService;
import webfx.platforms.core.services.querypush.PulseArgument;
import webfx.platforms.core.services.querypush.QueryPushArgument;
import webfx.platforms.core.services.querypush.QueryPushResult;
import webfx.platforms.core.services.querypush.QueryPushService;
import webfx.platforms.core.services.querypush.diff.QueryResultComparator;
import webfx.platforms.core.services.querypush.diff.QueryResultDiff;
import webfx.platforms.core.services.querypush.spi.QueryPushServiceProvider;
import webfx.platforms.core.services.scheduler.Scheduler;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.collection.Collections;

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
        synchronized (this) {
            if (pulsePass == null || pulsePass.isFinished()) {
                //Logger.log("Starting new pulse pass");
                pulsePass = createPulsePass(argument);
            } else {
                //Logger.log("Updating pulse pass");
                pulsePass.applyPulseArgument(argument);
            }
        }
    }

    abstract PulsePass createPulsePass(PulseArgument argument);

    protected abstract void setStreamQueryArgument(StreamInfo streamInfo, QueryArgument queryArgument);

    protected abstract void removeStream(StreamInfo streamInfo);

    protected abstract void removePushClientStreams(Object pushClientId);

    final class StreamInfo {
        final long creationTime = now();
        Object queryStreamId;
        final Object pushClientId;
        Boolean active;
        Boolean close;
        QueryInfo queryInfo;
        QueryResult lastQueryResult;

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
            // Resetting lastQueryResult to null will force to send the whole result on next push
            lastQueryResult = null;
        }
    }

    public static final class QueryInfo {
        final QueryArgument queryArgument;
        private final List<StreamInfo> streamInfos = new ArrayList<>(); // Contains new client streams that haven't received any result yet
        int activeNewStreamCount;
/*
        private final List<StreamInfo> blankStreamInfos = new ArrayList<>(); // Contains new client streams that haven't received any result yet
        private final List<StreamInfo> filledStreamInfos = new ArrayList<>(); // Contains client streams that already received at least 1 result
*/
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
                streamInfos.add(streamInfo);
                if (streamInfo.active) {
                    activeStreamCount++;
                    if (streamInfo.lastQueryResult != lastQueryResult)
                        activeNewStreamCount++;
                }
            }
        }

        void removeStreamInfo(StreamInfo streamInfo) {
            synchronized (this) {
                if (streamInfos.remove(streamInfo)) {
                    if (streamInfo.active) {
                        activeStreamCount--;
                        if (streamInfo.lastQueryResult != lastQueryResult)
                            activeNewStreamCount--;
                    }
                }
            }
        }

        int getActiveNewStreamCount() {
            return activeNewStreamCount;
        }

/*
        void markBlankStreamsAsFilled() {
            synchronized (this) {
                // Moving the blank streams into the filled ones
                filledStreamInfos.addAll(blankStreamInfos);
                blankStreamInfos.clear();
            }
        }
*/

        void updateActiveStreamCount(boolean increment) {
            activeStreamCount += increment ? 1 : -1;
        }

        boolean hasNoMoreStreams() {
            return streamInfos.isEmpty();
        }
    }

    abstract class PulsePass {
        final long startTime = now();
        int executedQueries, changedQueries, pushedToClients, pushedFailed;
        QueryInfo nextMostUrgentQueryNotYetRefreshed;
        boolean finished;

        PulsePass(PulseArgument argument) {
            applyPulseArgument(argument);
            refreshNextMostUrgentQueryIfAnyAndLoop();
        }

        abstract void applyPulseArgument(PulseArgument argument);

        void refreshNextMostUrgentQueryIfAnyAndLoop() {
            QueryInfo nextMostUrgentQuery = getNextMostUrgentQuery();
            if (nextMostUrgentQuery == null)
                markAsFinished();
            else
                refreshQuery(nextMostUrgentQuery).setHandler(ar -> refreshNextMostUrgentQueryIfAnyAndLoop());
        }

        Future<Void> refreshQuery(QueryInfo queryInfo) {
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
            synchronized (queryInfo) {
                // Retrieving the last result
                QueryResult lastQueryResult = queryInfo.lastQueryResult;
                List<StreamInfo> activeNewClients = Collections.filter(queryInfo.streamInfos, si -> si.isActive() && si.lastQueryResult != lastQueryResult);
                List<StreamInfo> activeOldClients = Collections.filter(queryInfo.streamInfos, si -> si.isActive() && si.lastQueryResult == lastQueryResult);
                queryInfo.activeNewStreamCount = activeNewClients.size();
                // Checking if there are changes compared to the last result - false if there are no "old" clients (that have already received the last result)
                boolean hasChanged = !activeOldClients.isEmpty() && queryInfo.hasQueryResultChanged(queryResult);
                if (hasChanged) // Increasing the changed queries counter in this case
                    changedQueries++;
                // Setting the version number (same if no change or +1 if changed)
                if (lastQueryResult != null)
                    queryResult.setVersionNumber(lastQueryResult.getVersionNumber() + (hasChanged ? 1 : 0));
                // Computing the diff in case of changes
                QueryResultDiff queryResultDiff = hasChanged ? QueryResultComparator.computeDiff(lastQueryResult, queryResult) : null;
                // Sending the whole query result to new clients (the blank streams that haven't received any result yet)
                pushResultToClients(activeNewClients, queryResult, null);
                // Sending only the diff to old clients (or sending the whole result if the comparator couldn't compute a diff)
                if (hasChanged)
                    pushResultToClients(activeOldClients, queryResult, queryResultDiff);
            }
        }

        void pushResultToClients(Collection<StreamInfo> streamInfos, QueryResult queryResult, QueryResultDiff queryResultDiff) {
/*
            if (queryResultDiff != null)
                Logger.log("Pushing diff " + queryResultDiff.getPreviousQueryResultVersionNumber() + " -> " + queryResultDiff.getFinalQueryResultVersionNumber() + " to " + streamInfos.size() + " clients");
            else
                Logger.log("Pushing result " + queryResult.getVersionNumber() + " to " + streamInfos.size() + " clients");
*/
            for (StreamInfo streamInfo : streamInfos) {
                pushedToClients++;
                pushResultToClient(streamInfo, queryResult, queryResultDiff);
            }
        }

        void pushResultToClient(StreamInfo streamInfo, QueryResult queryResult, QueryResultDiff queryResultDiff) {
            streamInfo.lastQueryResult = queryResult;
            QueryPushService.pushQueryResultToClient(new QueryPushResult(streamInfo.queryStreamId, queryResult, queryResultDiff), streamInfo.pushClientId)
                .setHandler(ar -> {
                    if (ar.failed()) { // Handling push call failure
                        long timeSinceCreation = now() - streamInfo.creationTime;
                        if (timeSinceCreation < 1_000)
                            Scheduler.scheduleDelay(100, () -> {
                                Logger.log("Retrying result push to client " + streamInfo.pushClientId + " since it failed less than 1s after the stream creation (the client push registration may have not been completed)");
                                pushResultToClient(streamInfo, queryResult, queryResultDiff);
                            });
                        else {
                            Logger.log("Result push failed :" + ar.cause().getMessage());
                            pushedFailed++;
                            removeStream(streamInfo);
                        }
                    }
                });
        }

        QueryInfo getNextMostUrgentQuery() {
            QueryInfo nextMostUrgentQuery = getNextMostUrgentQueryNotYetRefreshed();
            nextMostUrgentQueryNotYetRefreshed = null;
            return nextMostUrgentQuery;
        }

        QueryInfo getNextMostUrgentQueryNotYetRefreshed() {
            if (nextMostUrgentQueryNotYetRefreshed == null)
                nextMostUrgentQueryNotYetRefreshed = fetchNextMostUrgentQuery();
            return nextMostUrgentQueryNotYetRefreshed;
        }

        abstract QueryInfo fetchNextMostUrgentQuery();

        public void markAsFinished() {
            finished = true;
            onFinished();
        }

        boolean isFinished() {
            return finished;
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
