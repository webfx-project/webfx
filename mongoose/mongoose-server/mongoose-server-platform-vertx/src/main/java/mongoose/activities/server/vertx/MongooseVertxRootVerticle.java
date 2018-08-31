package mongoose.activities.server.vertx;

import mongoose.activities.server.SystemMetricsRecorderActivity;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.platform.services.json.Json;
import webfx.platform.services.json.JsonObject;
import webfx.platform.services.datasource.ConnectionDetails;
import webfx.platform.services.datasource.LocalDataSourceRegistry;
import webfx.platform.services.log.Logger;
import webfx.platform.services.push.server.PushServerService;
import webfx.platform.services.resource.ResourceService;
import webfx.platform.services.update.UpdateArgument;
import webfx.platform.services.update.UpdateService;
import webfx.providers.platform.server.vertx.util.VertxRunner;
import webfx.providers.platform.server.vertx.verticles.RootVerticle;

/**
 * @author Bruno Salmon
 */
public class MongooseVertxRootVerticle extends RootVerticle {

    public static void main(String[] args) {
        VertxRunner.runVerticle(MongooseVertxRootVerticle.class);
    }

    @Override
    public void start() throws Exception {
        super.start();
        registerMongooseLocalDataSource();
        SystemMetricsRecorderActivity.startAsServerActivity();
    }

    private static void registerMongooseLocalDataSource() {
        DataSourceModel dataSourceModel = DomainModelSnapshotLoader.getDataSourceModel();
        dataSourceModel.getDomainModel();
        Object dataSourceId = dataSourceModel.getId();
        String json = ResourceService.getText("mongoose/datasource/" + dataSourceId + "/ConnectionDetails.json").result();
        JsonObject jso = json == null ? null : Json.parseObject(json);
        ConnectionDetails connectionDetails = ConnectionDetails.fromJson(jso);
        if (connectionDetails != null)
            LocalDataSourceRegistry.registerLocalDataSource(dataSourceId, connectionDetails);
        PushServerService.addPushClientDisconnectListener(pushClientId ->
            UpdateService.executeUpdate(new UpdateArgument("update session_connection set \"end\"=now() where process_id=?", new Object[]{pushClientId}, dataSourceId))
                .setHandler(ar -> {
                    if (ar.failed())
                        Logger.log(ar.cause());
                }));
/* Commented because executing it here is too early (the clients didn't have a chance to reconnect so all ping would fail)
        QueryService.executeQuery(new QueryArgument("select distinct process_id from session_connection where \"end\" is null", dataSourceId))
            .setHandler(ar -> {
                if (ar.failed())
                    Logger.log(ar.cause());
                else {
                    QueryResult rs = ar.result();
                    for (int i = 0, n = rs.getRowCount(); i < n; i++)
                        PushServerService.pingPushClient(Platform.bus(), rs.getValue(i, 0));
                }
            });
*/
    }

}
