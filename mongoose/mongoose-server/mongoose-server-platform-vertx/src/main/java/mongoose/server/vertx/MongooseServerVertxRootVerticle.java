package mongoose.server.vertx;

import mongooses.core.activities.server.SystemMetricsRecorderActivity;
import mongooses.core.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.platforms.core.services.json.Json;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.datasource.ConnectionDetails;
import webfx.platforms.core.services.datasource.LocalDataSourceRegistry;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.push.server.PushServerService;
import webfx.platforms.core.services.resource.ResourceService;
import webfx.platforms.core.services.update.UpdateArgument;
import webfx.platforms.core.services.update.UpdateService;
import webfx.platform.vertx.util.VertxRunner;
import webfx.platform.vertx.verticles.RootVerticle;

/**
 * @author Bruno Salmon
 */
public class MongooseServerVertxRootVerticle extends RootVerticle {

    public static void main(String[] args) {
        VertxRunner.runVerticle(MongooseServerVertxRootVerticle.class);
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
        String json = ResourceService.getText("mongooses/core/datasource/" + dataSourceId + "/ConnectionDetails.json").result();
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
