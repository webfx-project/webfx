package mongoose.authz;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.services.authz.spi.impl.AuthorizationServiceProviderBase;
import naga.framework.services.authz.spi.impl.UserPrincipalAuthorizationChecker;

/**
 * @author Bruno Salmon
 */
public class MongooseAuthorizationServiceProvider extends AuthorizationServiceProviderBase {

    private final DataSourceModel dataSourceModel;

    public MongooseAuthorizationServiceProvider() {
        this(DomainModelSnapshotLoader.getDataSourceModel());
    }

    public MongooseAuthorizationServiceProvider(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    protected UserPrincipalAuthorizationChecker createUserPrincipalAuthorizationChecker(Object userPrincipal) {
        return new MongooseInMemoryUserPrincipalAuthorizationChecker(userPrincipal, dataSourceModel);
    }
}
