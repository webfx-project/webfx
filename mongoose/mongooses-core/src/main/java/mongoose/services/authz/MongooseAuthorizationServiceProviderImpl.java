package mongoose.services.authz;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.services.authz.spi.impl.AuthorizationServiceProviderImplBase;
import naga.framework.services.authz.spi.impl.UserPrincipalAuthorizationChecker;

/**
 * @author Bruno Salmon
 */
public class MongooseAuthorizationServiceProviderImpl extends AuthorizationServiceProviderImplBase {

    private final DataSourceModel dataSourceModel;

    public MongooseAuthorizationServiceProviderImpl() {
        this(DomainModelSnapshotLoader.getDataSourceModel());
    }

    public MongooseAuthorizationServiceProviderImpl(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    protected UserPrincipalAuthorizationChecker createUserPrincipalAuthorizationChecker(Object userPrincipal) {
        return new MongooseInMemoryUserPrincipalAuthorizationChecker(userPrincipal, dataSourceModel);
    }
}
