package mongoose.shared.services.datasourcemodel;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.dbms.PostgresSyntax;
import webfx.framework.shared.services.datasourcemodel.spi.DataSourceModelProvider;
import webfx.framework.shared.services.domainmodel.DomainModelService;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public final class MongooseDataSourceModelProvider implements DataSourceModelProvider {

    public final static Object MONGOOSE_DATA_SOURCE_ID = "MDS";

    private final static DataSourceModel MONGOOSE_DATA_SOURCE_MODEL = new DataSourceModel(
            MONGOOSE_DATA_SOURCE_ID,
            PostgresSyntax.get(),
            DomainModelService.loadDomainModel(MONGOOSE_DATA_SOURCE_ID).result()
    );

    @Override
    public Object getDefaultDataSourceId() {
        return MONGOOSE_DATA_SOURCE_ID;
    }

    @Override
    public DataSourceModel getDefaultDataSourceModel() {
        return MONGOOSE_DATA_SOURCE_MODEL;
    }

    @Override
    public DataSourceModel getDataSourceModel(Object dataSourceId) {
        return MONGOOSE_DATA_SOURCE_ID.equals(dataSourceId) ? MONGOOSE_DATA_SOURCE_MODEL : null;
    }

    @Override
    public Future<DataSourceModel> loadDataSourceModel(Object dataSourceId) {
        return MONGOOSE_DATA_SOURCE_ID.equals(dataSourceId) ? Future.succeededFuture(MONGOOSE_DATA_SOURCE_MODEL) : Future.failedFuture("Unknown data source model id=" + dataSourceId);
    }
}
