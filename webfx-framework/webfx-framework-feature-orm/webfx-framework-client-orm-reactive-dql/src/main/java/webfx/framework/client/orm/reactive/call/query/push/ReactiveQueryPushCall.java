package webfx.framework.client.orm.reactive.call.query.push;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import webfx.framework.client.orm.reactive.call.query.ReactiveQueryCall;
import webfx.framework.client.services.push.PushClientService;
import webfx.framework.shared.services.querypush.QueryPushArgument;
import webfx.framework.shared.services.querypush.QueryPushResult;
import webfx.framework.shared.services.querypush.QueryPushService;
import webfx.framework.shared.services.querypush.diff.QueryResultDiff;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class ReactiveQueryPushCall extends ReactiveQueryCall {

    private final List<ReactiveQueryPushCall> activeChildren = new ArrayList<>();
    private Object queryStreamId;
    private boolean waitingQueryStreamId;
    private boolean queryHasChangeWhileWaitingQueryStreamId;
    private boolean lostConnection;
    private boolean resend;
    private QueryResult lastQueryResult;

    public ReactiveQueryPushCall() {
        super(null);
    }

    private final ObjectProperty<Object> pushClientIdProperty = new SimpleObjectProperty<Object/*GWT*/>() {
        @Override
        protected void invalidated() {
            if (get() == null)
                lostConnection = true;
            else
                scheduleFireCallNowIfRequired();
        }
    };

    private final ObjectProperty<ReactiveQueryPushCall> activeParentProperty = new SimpleObjectProperty<ReactiveQueryPushCall/*GWT*/>() {
        private ReactiveQueryPushCall previousActiveParent;

        @Override
        protected void invalidated() {
            if (previousActiveParent != null)
                previousActiveParent.activeChildren.remove(ReactiveQueryPushCall.this);
            ReactiveQueryPushCall newActiveParent = get();
            if (newActiveParent != null)
                newActiveParent.activeChildren.add(ReactiveQueryPushCall.this);
            previousActiveParent = newActiveParent;
            scheduleFireCallNowIfRequired();
        }
    };

    public ReactiveQueryPushCall getActiveParent() {
        return activeParentProperty.get();
    }

    public ReactiveQueryPushCall setActiveParent(ReactiveQueryPushCall parent) {
        activeParentProperty.set(parent);
        return this;
    }

    public ObservableValue<Object> pushClientIdProperty() {
        return pushClientIdProperty;
    }

    public ReactiveQueryPushCall setPushClientId(Object pushClientId) {
        pushClientIdProperty.setValue(pushClientId);
        return this;
    }

    public Object getPushClientId() {
        return pushClientIdProperty.getValue();
    }

    @Override
    protected boolean isFireCallRequiredNow() {
        if (waitingQueryStreamId) // If we already wait the queryStreamId, we won't make a new call now (we can't update the stream without its id)
            queryHasChangeWhileWaitingQueryStreamId |= hasArgumentChangedSinceLastCall(); // but we mark this flag in order to update the stream (if modified) when receiving its id
        ReactiveQueryPushCall parent = getActiveParent();
        return  // pushClientId is null when the client is not yet connected to the server (so waiting the pushClientId before calling the server)
                isStarted() && getPushClientId() != null
                        && getArgument() != null
                        && !waitingQueryStreamId
                        // Skipping new stream not yet active (waiting it becomes active before calling the server)
                        && (queryStreamId != null || isActive())
                        && (parent == null || parent.queryStreamId != null && !parent.lostConnection);
    }

    @Override
    protected void resetStateBeforeCallingAsyncFunction() {
        super.resetStateBeforeCallingAsyncFunction();
        // Forgetting the queryStreamId on connection lost in case it is due to a server restart (the server will restart the sequence from 0 so we loose unity)
        if (lostConnection)
            queryStreamId = null; // This will force to resend the query argument and get a new id (TODO: make sequence persistent on server and remove this reset)
    }

    @Override
    protected void callAsyncFunction() {
        /* push mode -> possible multiple results pushed by the server */
        QueryArgument queryArgument = getArgument();
        // Network optimization: not necessary to send the query argument again if it has already been sent and hasn't change (the server kept a copy of it)
        QueryArgument transmittedQueryArgument = queryStreamId != null && !hasArgumentChangedSinceLastCall() ? null : queryArgument;
        if (transmittedQueryArgument != null && queryStreamId != null) {
            QueryArgument lastQueryArgument = getLastCallArgument();
            if (lastQueryArgument != null && lastQueryArgument.getStatement() != null && lastQueryArgument.getStatement().equals(queryArgument.getStatement()))
                transmittedQueryArgument = QueryArgument.builder().copy(queryArgument).setStatement(null).build();
        }
        memorizeLastCallArgument();
        ReactiveQueryPushCall parent = getActiveParent();
        Object parentQueryStreamId = parent == null ? null : parent.queryStreamId;
        waitingQueryStreamId = queryStreamId == null; // Setting the waitingQueryStreamId flag to true when queryStreamId is not yet known
        QueryPushService.executeQueryPush(QueryPushArgument.builder()
                .setQueryStreamId(queryStreamId)
                .setParentQueryStreamId(parentQueryStreamId)
                .setPushClientId(getPushClientId())
                .setQueryArgument(transmittedQueryArgument)
                .setActive(isActive())
                .setResend(resend)
                // This consumer will be called each time the server will push a change notification on the result
                .setQueryPushResultConsumer(queryPushResult -> onCallResult(computeQueryResult(queryPushResult), null))
                .build()
        ).setHandler(ar -> { // This handler is called only once when the query push service call returns
            boolean refreshChildren = false;
            // Cases where we need to trigger a new query push service call:
            if (ar.failed() // 1) on failure (this may happen if queryStreamId is not registered on the server anymore, for ex after server restart with a non persistent query push provider such as the in-memory default one)
                    || queryHasChangeWhileWaitingQueryStreamId) { // 2) when the query has changed while we were waiting for the query stream id
                log((isActive() ? "Refreshing queryStreamId=" + queryStreamId : "queryStreamId=" + queryStreamId + " will be refreshed when active") + (queryHasChangeWhileWaitingQueryStreamId ? " because the query has changed while waiting the queryStreamId" : " because a failure occurred while updating the query (may be an unrecognized queryStreamId after server restart)"));
                queryHasChangeWhileWaitingQueryStreamId = false; // Resetting the flag
                fireCallWhenReady(); // This will trigger an new pass (when active) leading to a new call to the query push service
            } else {
                resend = false;
                Logger.log("Ok " + ar.result());
                if (lostConnection || queryStreamId == null) {
                    lostConnection = false;
                    refreshChildren = true;
                }
            }
            queryStreamId = ar.result(); // the result is the queryStreamId returned by server (or null if failed)
            waitingQueryStreamId = false; // Resetting the waitingQueryStreamId flag to false
            if (refreshChildren)
                activeChildren.forEach(ReactiveQueryPushCall::fireCallWhenReady);
        });
        // Logging after the actual call (and not before) for optimization reason (better to log while the request is in process)
        log("Calling query push: queryStreamId=" + queryStreamId + ", parentQueryStreamId=" + parentQueryStreamId + ", pushClientId=" + getPushClientId() + ", active=" + isActive() + ", resend=" + resend + ", queryArgument=" + transmittedQueryArgument);
        // If the query argument hasn't changed, it's still possible that there is a change in the columns (but that didn't induce a change at the query level)
        if (transmittedQueryArgument == null) // Means the query argument hasn't change
            onQueryArgumentUnchanged();
    }

    protected void onQueryArgumentUnchanged() {
        //resetAllVisualResults(false); // So we reset now the display results with the current entities (and eventually new columns)
    }

    private QueryResult computeQueryResult(QueryPushResult queryPushResult) {
        //log("ReactiveQueryPushCall received QueryPushResult for queryStreamId=" + queryPushResult.getQueryStreamId());
        // Double checking if the query argument is still the latest
        if (hasArgumentChangedSinceLastCall()) {
            log("Skipping this QueryPushResult in ReactiveQueryPushCall as the argument has changed");
            return null;
        }
        QueryResult queryResult = queryPushResult.getQueryResult();
        // Rebuilding the query result in case only a diff has been sent
        QueryResultDiff diff = queryPushResult.getQueryResultDiff();
        if (queryResult == null && diff != null) {
            //log("Received diff " + diff.getPreviousQueryResultVersionNumber() + " -> " + diff.getFinalQueryResultVersionNumber());
            // Checking that the version number is correct
            if (lastQueryResult != null && diff.getPreviousQueryResultVersionNumber() == lastQueryResult.getVersionNumber())
                queryResult = diff.applyTo(lastQueryResult); // if correct, getting the new result by applying the diff to the last result
            else { // If not correct (the version numbers don't match - this may be due to a connection interruption)
                if (lastQueryResult != null && diff.getPreviousQueryResultVersionNumber() < lastQueryResult.getVersionNumber())
                    log("Ignoring an old received diff");
                else if (!resend) {
                    log("Refreshing queryStreamId=" + queryStreamId + " because of a received QueryResultDiff expecting another version number (" + diff.getPreviousQueryResultVersionNumber() + ") than the last QueryResult (" + (lastQueryResult == null ? "null" : lastQueryResult.getVersionNumber()) + ")");
                    resend = true; // Setting this flag to true will tell the server to resend the whole result and not only the diff on next push
                    fireCallWhenReady();
                }
                return null;
            }
        }
        // Keeping a reference to the query result (now considered as the last result)
        lastQueryResult = queryResult;
        return queryResult;
    }

    @Override
    protected void onStarted() {
        pushClientIdProperty.bind(PushClientService.pushClientIdProperty());
        super.onStarted();
    }

    @Override
    protected void onStopped() {
        pushClientIdProperty.unbind();
        super.onStopped();
        // TODO: unregister the client on server side
    }
}
