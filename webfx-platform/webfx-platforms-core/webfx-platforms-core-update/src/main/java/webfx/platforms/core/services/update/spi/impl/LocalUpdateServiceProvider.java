package webfx.platforms.core.services.update.spi.impl;

import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.datasource.LocalDataSourceRegistry;
import webfx.platforms.core.services.update.LocalUpdateServiceRegistry;
import webfx.platforms.core.services.update.UpdateArgument;
import webfx.platforms.core.services.update.UpdateResult;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.core.util.async.Batch;
import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class LocalUpdateServiceProvider implements UpdateServiceProvider {

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
        throw new UnsupportedOperationException("This platform doesn't support remote update service");
    }

    protected Future<Batch<UpdateResult>> executeRemoteUpdateBatch(Batch<UpdateArgument> batch) {
        throw new UnsupportedOperationException("This platform doesn't support remote update service");
    }

}
