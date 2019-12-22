package webfx.platform.shared.services.update.spi.impl;

import webfx.platform.shared.services.datasource.LocalDataSource;
import webfx.platform.shared.services.update.LocalUpdateServiceRegistry;
import webfx.platform.shared.services.update.UpdateArgument;
import webfx.platform.shared.services.update.UpdateResult;
import webfx.platform.shared.services.update.spi.UpdateServiceProvider;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class LocalUpdateServiceProvider implements UpdateServiceProvider {

    @Override
    public Future<UpdateResult> executeUpdate(UpdateArgument argument) {
        UpdateServiceProvider localUpdateServiceProvider = getConnectedLocalUpdateService(argument.getDataSourceId());
        if (localUpdateServiceProvider != null)
            return localUpdateServiceProvider.executeUpdate(translateUpdate(argument));
        return executeRemoteUpdate(argument);
    }

    private UpdateArgument translateUpdate(UpdateArgument argument) {
        String updateLang = argument.getUpdateLang();
        if (updateLang != null) {
            Object dataSourceId = argument.getDataSourceId();
            String updateString = argument.getUpdateString();
            String translatedUpdate = LocalDataSource.get(dataSourceId).translateUpdateIntoDataSourceDefaultLanguage(updateLang, updateString);
            if (!updateString.equals(translatedUpdate)) {
                //Logger.log("Translated to: " + translatedUpdate);
                argument = new UpdateArgument(dataSourceId, argument.returnGeneratedKeys(), translatedUpdate, argument.getParameters());
            }
        }
        return argument;
    }

    @Override
    public Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        UpdateArgument[] batchArray = batch.getArray();
        if (batchArray.length == 0)
            return Future.succeededFuture(new Batch<>(new UpdateResult[0]));
        Object dataSourceId = batchArray[0].getDataSourceId();
        UpdateServiceProvider localUpdateServiceProvider = getConnectedLocalUpdateService(dataSourceId);
        if (localUpdateServiceProvider != null)
            return localUpdateServiceProvider.executeUpdateBatch(translateBatch(batch));
        return executeRemoteUpdateBatch(batch);
    }

    private Batch<UpdateArgument> translateBatch(Batch<UpdateArgument> batch) {
        return new Batch<>(Arrays.stream(batch.getArray()).map(this::translateUpdate).toArray(UpdateArgument[]::new));
    }

    protected UpdateServiceProvider getConnectedLocalUpdateService(Object dataSourceId) {
        UpdateServiceProvider connectedUpdateServiceProvider = LocalUpdateServiceRegistry.getLocalConnectedUpdateService(dataSourceId);
        if (connectedUpdateServiceProvider == null) {
            LocalDataSource localDataSource = LocalDataSource.get(dataSourceId);
            if (localDataSource != null) {
                connectedUpdateServiceProvider = createConnectedUpdateService(localDataSource);
                LocalUpdateServiceRegistry.registerLocalConnectedUpdateService(dataSourceId, connectedUpdateServiceProvider);
            }
        }
        return connectedUpdateServiceProvider;
    }

    protected UpdateServiceProvider createConnectedUpdateService(LocalDataSource localDataSource) {
        throw new UnsupportedOperationException("This platform doesn't support local update service");
    }

    protected Future<UpdateResult> executeRemoteUpdate(UpdateArgument argument) {
        throw new UnsupportedOperationException("This platform doesn't support remote update service");
    }

    protected Future<Batch<UpdateResult>> executeRemoteUpdateBatch(Batch<UpdateArgument> batch) {
        throw new UnsupportedOperationException("This platform doesn't support remote update service");
    }

}
