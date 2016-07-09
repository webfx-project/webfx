package naga.platform.services.update.remote;

import naga.platform.bus.call.BusCallServerActivity;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.platform.services.update.LocalUpdateServiceRegistry;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;
import naga.platform.services.update.spi.UpdateService;
import naga.commons.util.async.Future;

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

    protected UpdateService getConnectedLocalUpdateService(Object dataSourceId) {
        UpdateService connectedUpdateService = LocalUpdateServiceRegistry.getLocalConnectedUpdateService(dataSourceId);
        if (connectedUpdateService == null) {
            ConnectionDetails connectionDetails = LocalDataSourceRegistry.getLocalDataSourceConnectionDetails(dataSourceId);
            if (connectionDetails != null)
                connectedUpdateService = createConnectedUpdateService(connectionDetails);
        }
        return connectedUpdateService;
    }

    protected UpdateService createConnectedUpdateService(ConnectionDetails connectionDetails) {
        throw new UnsupportedOperationException("This platform doesn't support local update service");
    }

    protected Future<naga.platform.services.update.UpdateResult> executeRemoteUpdate(UpdateArgument argument) {
        return BusCallService.call(BusCallServerActivity.UPDATE_SERVICE_ADDRESS, argument);
    }

}
