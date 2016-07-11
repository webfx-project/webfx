package mongoose.server;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.activity.DomainActivityContext;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityManager;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;

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

    }

    protected static void startServerActivity(MongooseServerActivity mongooseServerActivity) {
        startServerActivity(mongooseServerActivity, null);
    }

    protected static void startServerActivity(MongooseServerActivity mongooseServerActivity, ConnectionDetails connectionDetails) {
        startServerActivity(mongooseServerActivity, DomainModelSnapshotLoader.getDataSourceModel(), connectionDetails);
    }

    protected static void startServerActivity(MongooseServerActivity mongooseServerActivity, DataSourceModel dataSourceModel, ConnectionDetails connectionDetails) {
        if (connectionDetails != null)
            LocalDataSourceRegistry.registerLocalDataSource(dataSourceModel.getId(), connectionDetails);
        ActivityManager.startServerActivity(mongooseServerActivity, DomainActivityContext.create(dataSourceModel));
    }

}
