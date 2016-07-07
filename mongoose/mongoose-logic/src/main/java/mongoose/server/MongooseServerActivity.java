package mongoose.server;

import mongoose.domainmodel.DomainModelSnapshotLoader;
import naga.core.activity.Activity;
import naga.core.activity.ActivityContext;
import naga.core.datasource.ConnectionDetails;
import naga.core.datasource.LocalDataSourceRegistry;
import naga.core.orm.domainmodel.DataSourceModel;

/**
 * @author Bruno Salmon
 */
public class MongooseServerActivity implements Activity {

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
    public void onCreate(ActivityContext context) {
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
