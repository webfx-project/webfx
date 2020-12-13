package dev.webfx.platform.shared.services.submit.spi.impl;

import dev.webfx.platform.shared.services.datasource.LocalDataSource;
import dev.webfx.platform.shared.services.submit.LocalSubmitServiceRegistry;
import dev.webfx.platform.shared.services.submit.SubmitArgument;
import dev.webfx.platform.shared.services.submit.SubmitResult;
import dev.webfx.platform.shared.services.submit.spi.SubmitServiceProvider;
import dev.webfx.platform.shared.util.async.Batch;
import dev.webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class LocalSubmitServiceProvider implements SubmitServiceProvider {

    @Override
    public Future<SubmitResult> executeSubmit(SubmitArgument argument) {
        SubmitServiceProvider localSubmitServiceProvider = getLocalConnectedSubmitService(argument.getDataSourceId());
        if (localSubmitServiceProvider != null)
            return localSubmitServiceProvider.executeSubmit(argument);
        return executeRemoteSubmit(argument);
    }

    @Override
    public Future<Batch<SubmitResult>> executeSubmitBatch(Batch<SubmitArgument> batch) {
        SubmitArgument[] batchArray = batch.getArray();
        if (batchArray.length == 0)
            return Future.succeededFuture(new Batch<>(new SubmitResult[0]));
        Object dataSourceId = batchArray[0].getDataSourceId();
        SubmitServiceProvider localSubmitServiceProvider = getLocalConnectedSubmitService(dataSourceId);
        if (localSubmitServiceProvider != null)
            return localSubmitServiceProvider.executeSubmitBatch(batch);
        return executeRemoteSubmitBatch(batch);
    }

    protected SubmitServiceProvider getLocalConnectedSubmitService(Object dataSourceId) {
        SubmitServiceProvider connectedSubmitServiceProvider = LocalSubmitServiceRegistry.getLocalConnectedSubmitService(dataSourceId);
        if (connectedSubmitServiceProvider == null) {
            LocalDataSource localDataSource = LocalDataSource.get(dataSourceId);
            if (localDataSource != null) {
                connectedSubmitServiceProvider = createLocalConnectedSubmitService(localDataSource);
                LocalSubmitServiceRegistry.registerLocalConnectedSubmitService(dataSourceId, connectedSubmitServiceProvider);
            }
        }
        return connectedSubmitServiceProvider;
    }

    protected SubmitServiceProvider createLocalConnectedSubmitService(LocalDataSource localDataSource) {
        throw new UnsupportedOperationException("This platform doesn't support local submit service");
    }

    protected Future<SubmitResult> executeRemoteSubmit(SubmitArgument argument) {
        throw new UnsupportedOperationException("This platform doesn't support remote submit service");
    }

    protected Future<Batch<SubmitResult>> executeRemoteSubmitBatch(Batch<SubmitArgument> batch) {
        throw new UnsupportedOperationException("This platform doesn't support remote submit service");
    }

}
