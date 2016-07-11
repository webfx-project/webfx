package mongoose.server;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.ui.activity.DomainActivityContext;
import naga.platform.activity.Activity;
import naga.platform.activity.ActivityContext;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.framework.orm.domainmodel.DataSourceModel;

/**
 * @author Bruno Salmon
 */
public class MongooseServerActivity implements Activity<DomainActivityContext> {

    protected ActivityContext activityContext;
    private final DataSourceModel dataSourceModel;
    private final ConnectionDetails connectionDetails;

    public MongooseServerActivity(ConnectionDetails connectionDetails) {
        this(DomainModelSnapshotLoader.getDataSourceModel(), connectionDetails);
    }

    public MongooseServerActivity(DataSourceModel dataSourceModel, ConnectionDetails connectionDetails) {
        this.dataSourceModel = dataSourceModel;
        this.connectionDetails = connectionDetails;
    }

    @Override
    public void onCreate(DomainActivityContext context) {
        this.activityContext = context;
        context.setDataSourceModel(dataSourceModel);
        LocalDataSourceRegistry.registerLocalDataSource(dataSourceModel.getId(), connectionDetails);
    }

    public ActivityContext getActivityContext() {
        return activityContext;
    }

    @Override
    public void onStart() {

    }
}
