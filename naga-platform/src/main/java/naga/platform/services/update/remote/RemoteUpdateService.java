package naga.platform.services.update.remote;

import naga.util.async.Batch;
import naga.platform.bus.call.BusCallServerActivity;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.platform.services.update.LocalUpdateServiceRegistry;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;
import naga.platform.services.update.spi.UpdateService;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class RemoteUpdateService implements UpdateService {

    public final static RemoteUpdateService REMOTE_ONLY_UPDATE_SERVICE = new RemoteUpdateService();

    @Override
    public Future<UpdateResult> executeUpdate(UpdateArgument argument) {
        UpdateService localUpdateService = getConnectedLocalUpdateService(argument.getDataSourceId());
        if (localUpdateService != null)
            return localUpdateService.executeUpdate(argument);
        return executeRemoteUpdate(argument);
    }

    @Override
    public Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
        Object dataSourceId = batch.getArray()[0].getDataSourceId();
        UpdateService localUpdateService = getConnectedLocalUpdateService(dataSourceId);
        if (localUpdateService != null)
            return localUpdateService.executeUpdateBatch(batch);
        return executeRemoteUpdateBatch(batch);
    }

    protected UpdateService getConnectedLocalUpdateService(Object dataSourceId) {
        UpdateService connectedUpdateService = LocalUpdateServiceRegistry.getLocalConnectedUpdateService(dataSourceId);
        if (connectedUpdateService == null) {
            ConnectionDetails connectionDetails = LocalDataSourceRegistry.getLocalDataSourceConnectionDetails(dataSourceId);
            if (connectionDetails != null) {
                connectedUpdateService = createConnectedUpdateService(connectionDetails);
                LocalUpdateServiceRegistry.registerLocalConnectedUpdateService(dataSourceId, connectedUpdateService);
            }
        }
        return connectedUpdateService;
    }

    protected UpdateService createConnectedUpdateService(ConnectionDetails connectionDetails) {
        throw new UnsupportedOperationException("This platform doesn't support local update service");
    }

    protected Future<UpdateResult> executeRemoteUpdate(UpdateArgument argument) {
        return BusCallService.call(BusCallServerActivity.UPDATE_SERVICE_ADDRESS, argument);
    }

    protected Future<Batch<UpdateResult>> executeRemoteUpdateBatch(Batch<UpdateArgument> batch) {
        return BusCallService.call(BusCallServerActivity.UPDATE_BATCH_SERVICE_ADDRESS, batch);
    }

}
