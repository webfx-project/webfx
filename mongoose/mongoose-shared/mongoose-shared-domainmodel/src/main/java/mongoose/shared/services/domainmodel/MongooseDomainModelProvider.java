package mongoose.shared.services.domainmodel;

import mongoose.shared.services.datasourcemodel.MongooseDataSourceModelProvider;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.services.domainmodel.spi.DomainModelProvider;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class MongooseDomainModelProvider implements DomainModelProvider {

    @Override
    public Future<DomainModel> loadDomainModel(Object dataSourceId) {
        if (!MongooseDataSourceModelProvider.MONGOOSE_DATA_SOURCE_ID.equals(dataSourceId))
            return Future.failedFuture("Unknown data source " + dataSourceId);
        return Future.succeededFuture(DomainModelSnapshotLoader.getOrLoadDomainModel());
    }
}
