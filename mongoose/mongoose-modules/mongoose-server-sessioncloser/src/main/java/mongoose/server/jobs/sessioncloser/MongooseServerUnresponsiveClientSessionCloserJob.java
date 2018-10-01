package mongoose.server.jobs.sessioncloser;

import mongoose.shared.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.platform.shared.services.appcontainer.spi.ApplicationJob;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.push.server.UnresponsivePushClientListener;
import webfx.platform.shared.services.push.server.PushServerService;
import webfx.platform.shared.services.update.UpdateArgument;
import webfx.platform.shared.services.update.UpdateService;

/**
 * @author Bruno Salmon
 */
public final class MongooseServerUnresponsiveClientSessionCloserJob implements ApplicationJob {

    private UnresponsivePushClientListener disconnectListener;

    @Override
    public void onStart() {
        Object dataSourceId = DomainModelSnapshotLoader.getDataSourceModel().getId();
        PushServerService.addUnresponsivePushClientListener(disconnectListener = pushClientId ->
                UpdateService.executeUpdate(new UpdateArgument("update session_connection set \"end\"=now() where process_id=?", new Object[]{pushClientId}, dataSourceId))
                        .setHandler(ar -> {
                            if (ar.failed())
                                Logger.log("Error while closing session for pushClientId=" + pushClientId, ar.cause());
                            else
                                Logger.log("Closed session for pushClientId=" + pushClientId);
                        }));
    }

    @Override
    public void onStop() {
        PushServerService.removeUnresponsivePushClientListener(disconnectListener);
    }

}
