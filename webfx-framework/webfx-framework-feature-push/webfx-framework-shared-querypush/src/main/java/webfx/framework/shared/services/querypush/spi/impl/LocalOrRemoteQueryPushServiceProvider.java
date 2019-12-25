package webfx.framework.shared.services.querypush.spi.impl;

import webfx.framework.shared.services.querypush.PulseArgument;
import webfx.framework.shared.services.querypush.QueryPushArgument;
import webfx.framework.shared.services.querypush.QueryPushResult;
import webfx.framework.shared.services.querypush.QueryPushService;
import webfx.framework.shared.services.querypush.spi.QueryPushServiceProvider;
import webfx.platform.shared.services.datasource.LocalDataSource;
import webfx.platform.shared.services.buscall.BusCallService;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.async.Future;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class LocalOrRemoteQueryPushServiceProvider implements QueryPushServiceProvider {

    @Override
    public Future<Object> executeQueryPush(QueryPushArgument argument) {
        QueryPushServiceProvider localConnectedProvider = getOrCreateLocalConnectedProvider(argument.getDataSourceId());
        if (localConnectedProvider != null)
            return localConnectedProvider.executeQueryPush(argument);
        return executeRemoteQueryPush(argument);
    }

    @Override
    public void executePulse(PulseArgument argument) {
        QueryPushServiceProvider localConnectedProvider = getOrCreateLocalConnectedProvider(argument.getDataSourceId());
        if (localConnectedProvider == null)
            throw new UnsupportedOperationException("requestPulse() shouldn't be called on this LocalOrRemoteQueryPushServiceProvider");
        localConnectedProvider.executePulse(argument);
    }

    protected QueryPushServiceProvider getOrCreateLocalConnectedProvider(Object dataSourceId) {
        QueryPushServiceProvider localConnectedProvider = LocalQueryPushServiceProviderRegistry.getLocalConnectedProvider(dataSourceId);
        if (localConnectedProvider == null) {
            LocalDataSource localDataSource = LocalDataSource.get(dataSourceId);
            if (localDataSource != null) {
                localConnectedProvider = createLocalConnectedProvider(localDataSource);
                LocalQueryPushServiceProviderRegistry.registerLocalConnectedProvider(dataSourceId, localConnectedProvider);
            }
        }
        return localConnectedProvider;
    }

    protected QueryPushServiceProvider createLocalConnectedProvider(LocalDataSource localDataSource) {
        throw new UnsupportedOperationException("This platform doesn't provide local QueryPushServiceProvider");
    }

    protected <T> Future<T> executeRemoteQueryPush(QueryPushArgument argument) {
        Consumer<QueryPushResult> queryPushResultConsumer = argument.getQueryPushResultConsumer();
        Object queryStreamId = argument.getQueryStreamId();
        boolean isConsumerRegistrationPendingCall = queryStreamId == null && queryPushResultConsumer != null;
        if (isConsumerRegistrationPendingCall)
            consumerRegistrationPendingCalls++;
        else if (queryStreamId != null && queryPushResultConsumer != null)
            queryPushResultConsumers.put(queryStreamId, queryPushResultConsumer);
        Future<T> call = BusCallService.call(QueryPushService.QUERY_PUSH_SERVICE_ADDRESS, argument);
        if (isConsumerRegistrationPendingCall)
            call = call.map(newQueryStreamId -> {
                synchronized (queryPushResultConsumers) {
                    // Registering the consumer along the returned queryStreamId
                    queryPushResultConsumers.put(newQueryStreamId, queryPushResultConsumer);
                    consumerRegistrationPendingCalls--; // Decreasing the pending calls now that the consumer is registered
                    // Retrying sending results with no consumer if any
                    if (!withNoConsumerReceivedResults.isEmpty()) {
                        // Working on a copy to avoid concurrent modification (and also infinite recursion)
                        ArrayList<QueryPushResult> copy = new ArrayList<>(withNoConsumerReceivedResults);
                        withNoConsumerReceivedResults.clear();
                        // Now retrying
                        copy.forEach(LocalOrRemoteQueryPushServiceProvider::onQueryPushResultReceived);
                        // If at this point withNoConsumerReceivedResults is not empty, this is probably because the
                        // result has been received before the queryStreamId (can happen especially for a cached query)
                        // but it will be considered on next push result received
                        // TODO check if it is ok like this or if we should explicitly call onQueryPushResultReceived() when receiving the queryStreamId
                    }
                    return newQueryStreamId;
                }
            });
        return call;
    }

    private final static Map<Object, Consumer<QueryPushResult>> queryPushResultConsumers = new HashMap<>();
    private static int consumerRegistrationPendingCalls;
    private final static List<QueryPushResult> withNoConsumerReceivedResults = new ArrayList<>();

    public static void onQueryPushResultReceived(QueryPushResult qpr) {
        //Logger.log("LocalOrRemoteQueryPushServiceProvider received QueryPushResult for " + qpr.getQueryStreamId());
        synchronized (queryPushResultConsumers) {
            Consumer<QueryPushResult> consumer = queryPushResultConsumers.get(qpr.getQueryStreamId());
            if (consumer != null)
                consumer.accept(qpr);
            else if (consumerRegistrationPendingCalls > 0) // Consumer not found but this may be because this result has been received before the registration call returns
                withNoConsumerReceivedResults.add(qpr); // we will retry on next registration call return (see executeRemoteQueryPush)
            else // Definitely no consumer registered along that queryStreamId
                Logger.log("QueryPushResult received with unregistered queryStreamId = " + qpr.getQueryStreamId());
        }
    }
}
