package naga.platform.services.querypush.spi.impl;

import naga.platform.services.log.Logger;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryService;
import naga.platform.services.querypush.PulseArgument;
import naga.platform.services.querypush.QueryPushArgument;
import naga.platform.services.querypush.QueryPushResult;
import naga.platform.services.querypush.QueryPushService;
import naga.platform.services.querypush.spi.QueryPushServiceProvider;
import naga.util.async.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class QueryPushServiceProviderBase implements QueryPushServiceProvider {
    private PulsePass pulsePass;

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

    Future<Void> executeQueryAndPushResult(QueryInfo queryInfo) {
        queryInfo.touch();
        return QueryService.executeQuery(queryInfo.queryArgument).map(rs -> {
            for (StreamInfo streamInfo : new ArrayList<>(queryInfo.streamInfos))
                QueryPushService.pushQueryResultToClient(new QueryPushResult(streamInfo.queryStreamId, rs), streamInfo.pushClientId)
                    .setHandler(ar -> {
                        if (ar.failed())
                            removeStream(streamInfo);
                    });
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
        final List<StreamInfo> streamInfos = new ArrayList<>();
        int activeStreamCount;
        long lastQueryExecutionTime;
        long lastPossibleChangeTime;

        QueryInfo(QueryArgument queryArgument) {
            this.queryArgument = queryArgument;
        }

        void touch() {
            lastQueryExecutionTime = now();
            lastPossibleChangeTime = 0;
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
            streamInfos.add(streamInfo);
            if (streamInfo.active)
                activeStreamCount++;
        }

        void removeStreamInfo(StreamInfo streamInfo) {
            streamInfos.remove(streamInfo);
            if (streamInfo.active)
                activeStreamCount--;
        }

        void updateActiveStreamCount(boolean increment) {
            activeStreamCount += increment ? 1 : -1;
        }
    }

    abstract class PulsePass {
        final long startTime = now();
        int queries;
        QueryInfo nextHottestQueryNotYetReturned;
        boolean finished;

        PulsePass(PulseArgument argument) {
            applyPulseArgument(argument);
            iterate();
        }

        abstract void applyPulseArgument(PulseArgument argument);

        void iterate() {
            if (isFinished())
                onFinished();
            else {
                queries++;
                executeQueryAndPushResult(getNextHottestQuery()).setHandler(ar -> iterate());
            }
        }

        QueryInfo getNextHottestQuery() {
            QueryInfo nextHottestQuery = getNextHottestQueryNotYetReturned();
            nextHottestQueryNotYetReturned = null;
            return nextHottestQuery;
        }

        QueryInfo getNextHottestQueryNotYetReturned() {
            if (nextHottestQueryNotYetReturned == null)
                nextHottestQueryNotYetReturned = fetchNextHottestQuery();
            return nextHottestQueryNotYetReturned;
        }

        abstract QueryInfo fetchNextHottestQuery();

        boolean isFinished() {
            finished |= getNextHottestQueryNotYetReturned() == null;
            return finished;
        }

        void onFinished() {
            Logger.log("Pulse finished (" + queries + " queries in " + (now() - startTime) + "ms)");
        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }

}
