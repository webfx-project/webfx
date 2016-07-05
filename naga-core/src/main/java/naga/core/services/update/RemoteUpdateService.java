package naga.core.services.update;

import naga.core.Naga;
import naga.core.bus.call.BusCallService;
import naga.core.datasource.ConnectionDetails;
import naga.core.datasource.LocalDataSourceRegistry;
import naga.core.util.async.Future;

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

    protected Future<naga.core.services.update.UpdateResult> executeRemoteUpdate(UpdateArgument argument) {
        return BusCallService.call(Naga.UPDATE_SERVICE_ADDRESS, argument);
    }

}
