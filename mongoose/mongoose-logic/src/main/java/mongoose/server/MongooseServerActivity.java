package mongoose.server;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.activity.DomainActivityContext;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class MongooseServerActivity implements Activity<DomainActivityContext> {

    protected DomainActivityContext activityContext;

    @Override
    public void onCreate(DomainActivityContext context) {
        this.activityContext = context;
    }

    public DomainActivityContext getActivityContext() {
        return activityContext;
    }

    @Override
    public void onStart() {
        Platform.log("Starting Mongoose server activity...");
    }

    public static void startServerActivity() {
        startServerActivity(new MongooseServerActivity());
    }

    public static void startServerActivity(MongooseServerActivity mongooseServerActivity) {
        DataSourceModel dataSourceModel = DomainModelSnapshotLoader.getDataSourceModel();
        String json = Platform.getResourceService().getText("mongoose/datasource/" + dataSourceModel.getId() + "/ConnectionDetails.json").result();
        JsonObject jso = json == null ? null : Json.parseObject(json);
        startServerActivity(mongooseServerActivity, dataSourceModel, ConnectionDetails.fromJson(jso));
    }

    public static void startServerActivity(MongooseServerActivity mongooseServerActivity, DataSourceModel dataSourceModel, ConnectionDetails connectionDetails) {
        if (connectionDetails != null)
            LocalDataSourceRegistry.registerLocalDataSource(dataSourceModel.getId(), connectionDetails);
        ActivityManager.startServerActivity(mongooseServerActivity, DomainActivityContext.create(dataSourceModel));
    }

}
