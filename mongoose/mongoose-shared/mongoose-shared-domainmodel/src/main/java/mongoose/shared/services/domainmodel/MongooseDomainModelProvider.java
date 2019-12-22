package mongoose.shared.services.domainmodel;

import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.services.domainmodelloader.spi.DomainModelLoaderProvider;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class MongooseDomainModelProvider implements DomainModelLoaderProvider {

    @Override
    public Future<DomainModel> loadDomainModel(Object dataSourceId) {
        return Future.succeededFuture(DomainModelSnapshotLoader.getOrLoadDomainModel());
    }
}
