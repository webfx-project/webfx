package naga.platform.services.query.push.spi.remote;

import naga.platform.bus.call.BusCallServerActivity;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.platform.services.log.Logger;
import naga.platform.services.query.QueryResult;
import naga.platform.services.query.push.PulseArgument;
import naga.platform.services.query.push.QueryPushArgument;
import naga.platform.services.query.push.QueryPushResult;
import naga.platform.services.query.push.QueryPushService;
import naga.platform.services.query.push.spi.QueryPushServiceProvider;
import naga.util.async.Future;
import naga.util.function.Consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static naga.platform.services.query.push.spi.remote.LocalQueryPushServiceProviderRegistry.getLocalConnectedProvider;
import static naga.platform.services.query.push.spi.remote.LocalQueryPushServiceProviderRegistry.registerLocalConnectedProvider;

/**
 * @author Bruno Salmon
 */
public class RemoteQueryPushServiceProvider implements QueryPushServiceProvider {

    @Override
    public Future<Object> executeQueryPush(QueryPushArgument argument) {
        QueryPushServiceProvider localConnectedProvider = getOrCreateLocalConnectedProvider(argument.getDataSourceId());
        if (localConnectedProvider != null)
            return localConnectedProvider.executeQueryPush(argument);
        return executeRemoteQueryPush(argument);
    }

    @Override
    public void requestPulse(PulseArgument argument) {
        QueryPushServiceProvider localConnectedProvider = getOrCreateLocalConnectedProvider(argument.getDataSourceId());
        if (localConnectedProvider == null)
            throw new UnsupportedOperationException("requestPulse() shouldn't be called on this RemoteQueryPushServiceProvider");
        localConnectedProvider.requestPulse(argument);
    }

    protected QueryPushServiceProvider getOrCreateLocalConnectedProvider(Object dataSourceId) {
        QueryPushServiceProvider localConnectedProvider = getLocalConnectedProvider(dataSourceId);
        if (localConnectedProvider == null) {
            ConnectionDetails connectionDetails = LocalDataSourceRegistry.getLocalDataSourceConnectionDetails(dataSourceId);
            if (connectionDetails != null) {
                localConnectedProvider = createLocalConnectedProvider(connectionDetails);
                registerLocalConnectedProvider(dataSourceId, localConnectedProvider);
            }
        }
        return localConnectedProvider;
    }

    protected QueryPushServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        throw new UnsupportedOperationException("This platform doesn't provide local QueryPushServiceProvider");
    }

    protected <T> Future<T> executeRemoteQueryPush(QueryPushArgument argument) {
        boolean isConsumerRegistrationPendingCall = argument.getQueryResultConsumer() != null;
        if (isConsumerRegistrationPendingCall)
            consumerRegistrationPendingCalls++;
        Future<T> call = BusCallService.call(BusCallServerActivity.QUERY_PUSH_SERVICE_ADDRESS, argument);
        if (isConsumerRegistrationPendingCall)
            call = call.map(queryStreamId -> {
                synchronized (queryResultConsumers) {
                    // Registering the consumer along the returned queryStreamId
                    queryResultConsumers.put(queryStreamId, argument.getQueryResultConsumer());
                    consumerRegistrationPendingCalls--; // Decreasing the pending calls now that the consumer is registered
                    // Retrying sending results with no consumer if any
                    for (QueryPushResult qpr : withNoConsumerReceivedResults)
                        onQueryPushResultReceived(qpr);
                    withNoConsumerReceivedResults.clear();
                    return queryStreamId;
                }
            });
        return call;
    }

    private final static Map<Object, Consumer<QueryResult>> queryResultConsumers = new HashMap<>();
    private static int consumerRegistrationPendingCalls;
    private final static List<QueryPushResult> withNoConsumerReceivedResults = new ArrayList<>();

    private static void onQueryPushResultReceived(QueryPushResult qpr) {
        synchronized (queryResultConsumers) {
            Consumer<QueryResult> queryResultConsumer = queryResultConsumers.get(qpr.getQueryStreamId());
            if (queryResultConsumer != null)
                queryResultConsumer.accept(qpr.getQueryResult());
            else if (consumerRegistrationPendingCalls > 0) // Consumer not found but this may be because this result has been received before the registration call returns
                withNoConsumerReceivedResults.add(qpr); // we will retry on next registration call return (see executeRemoteQueryPush)
            else // Definitely no consumer registered along that queryStreamId
                Logger.log("QueryPushResult received with unregistered queryStreamId = " + qpr.getQueryStreamId());
        }
    }

    static {
        QueryPushService.registerQueryPushClientConsumer(RemoteQueryPushServiceProvider::onQueryPushResultReceived);
    }

}
