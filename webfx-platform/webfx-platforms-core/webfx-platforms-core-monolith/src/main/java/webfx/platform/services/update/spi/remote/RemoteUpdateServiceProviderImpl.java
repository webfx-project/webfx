package webfx.platform.services.update.spi.remote;

import webfx.platform.services.update.spi.UpdateServiceProvider;
import webfx.util.async.Batch;
import webfx.platform.bus.call.BusCallServerModule;
import webfx.platform.bus.call.BusCallService;
import webfx.platform.services.datasource.ConnectionDetails;
import webfx.platform.services.datasource.LocalDataSourceRegistry;
import webfx.platform.services.update.LocalUpdateServiceRegistry;
import webfx.platform.services.update.UpdateArgument;
import webfx.platform.services.update.UpdateResult;
import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class RemoteUpdateServiceProviderImpl implements UpdateServiceProvider {

    @Override
    public Future<UpdateResult> executeUpdate(UpdateArgument argument) {
        UpdateServiceProvider localUpdateServiceProvider = getConnectedLocalUpdateService(argument.getDataSourceId());
        if (localUpdateServiceProvider != null)
            return localUpdateServiceProvider.executeUpdate(argument);
        return executeRemoteUpdate(argument);
    }

    @Override
    public Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        UpdateArgument[] batchArray = batch.getArray();
        if (batchArray.length == 0)
            return Future.succeededFuture(new Batch<>(new UpdateResult[0]));
        Object dataSourceId = batchArray[0].getDataSourceId();
        UpdateServiceProvider localUpdateServiceProvider = getConnectedLocalUpdateService(dataSourceId);
        if (localUpdateServiceProvider != null)
            return localUpdateServiceProvider.executeUpdateBatch(batch);
        return executeRemoteUpdateBatch(batch);
    }

    protected UpdateServiceProvider getConnectedLocalUpdateService(Object dataSourceId) {
        UpdateServiceProvider connectedUpdateServiceProvider = LocalUpdateServiceRegistry.getLocalConnectedUpdateService(dataSourceId);
        if (connectedUpdateServiceProvider == null) {
            ConnectionDetails connectionDetails = LocalDataSourceRegistry.getLocalDataSourceConnectionDetails(dataSourceId);
            if (connectionDetails != null) {
                connectedUpdateServiceProvider = createConnectedUpdateService(connectionDetails);
                LocalUpdateServiceRegistry.registerLocalConnectedUpdateService(dataSourceId, connectedUpdateServiceProvider);
            }
        }
        return connectedUpdateServiceProvider;
    }

    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        throw new UnsupportedOperationException("This platform doesn't support local update service");
    }

    protected Future<UpdateResult> executeRemoteUpdate(UpdateArgument argument) {
        return BusCallService.call(BusCallServerModule.UPDATE_SERVICE_ADDRESS, argument);
    }

    protected Future<Batch<UpdateResult>> executeRemoteUpdateBatch(Batch<UpdateArgument> batch) {
        return BusCallService.call(BusCallServerModule.UPDATE_BATCH_SERVICE_ADDRESS, batch);
    }

}
