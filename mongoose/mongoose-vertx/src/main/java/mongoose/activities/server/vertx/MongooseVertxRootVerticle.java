package mongoose.activities.server.vertx;

import mongoose.activities.server.MongooseServerMetricsActivity;
import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.platform.services.log.Logger;
import naga.platform.services.push.server.PushServerService;
import naga.platform.services.resource.ResourceService;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateService;
import naga.providers.platform.server.vertx.util.VertxRunner;
import naga.providers.platform.server.vertx.verticles.RootVerticle;

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
        MongooseServerMetricsActivity.startActivity();
        // MongooseServerPushActivity.startActivity(); // Now replaced by the query push service provider
    }

    private static void registerMongooseLocalDataSource() {
        DataSourceModel dataSourceModel = DomainModelSnapshotLoader.getDataSourceModel();
        String json = ResourceService.getText("mongoose/datasource/" + dataSourceModel.getId() + "/ConnectionDetails.json").result();
        JsonObject jso = json == null ? null : Json.parseObject(json);
        ConnectionDetails connectionDetails = ConnectionDetails.fromJson(jso);
        if (connectionDetails != null)
            LocalDataSourceRegistry.registerLocalDataSource(dataSourceModel.getId(), connectionDetails);
    }

}
