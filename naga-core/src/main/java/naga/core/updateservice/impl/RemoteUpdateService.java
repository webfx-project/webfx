package naga.core.updateservice.impl;

import naga.core.Naga;
import naga.core.bus.call.BusCallService;
import naga.core.datasource.ConnectionDetails;
import naga.core.datasource.LocalDataSourceRegistry;
import naga.core.updateservice.UpdateArgument;
import naga.core.updateservice.UpdateResult;
import naga.core.updateservice.UpdateService;
import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class RemoteUpdateService implements UpdateService {

    public final static RemoteUpdateService REMOTE_ONLY_UPDATE_SERVICE = new RemoteUpdateService();

    @Override
    public Future<UpdateResult> update(UpdateArgument argument) {
        UpdateService localUpdateService = getConnectedLocalUpdateService(argument.getDataSourceId());
        if (localUpdateService != null)
            return localUpdateService.update(argument);
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

    protected Future<naga.core.updateservice.UpdateResult> executeRemoteUpdate(UpdateArgument argument) {
        return BusCallService.call(Naga.QUERY_WRITE_ADDRESS, argument);
    }

}
