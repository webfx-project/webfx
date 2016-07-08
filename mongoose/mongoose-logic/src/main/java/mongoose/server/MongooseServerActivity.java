package mongoose.server;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.commons.activity.Activity;
import naga.commons.activity.ActivityContext;
import naga.commons.services.datasource.ConnectionDetails;
import naga.commons.services.datasource.LocalDataSourceRegistry;
import naga.framework.orm.domainmodel.DataSourceModel;

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
