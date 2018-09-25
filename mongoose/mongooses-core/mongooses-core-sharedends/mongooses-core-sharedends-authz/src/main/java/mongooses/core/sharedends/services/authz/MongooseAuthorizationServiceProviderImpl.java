package mongooses.core.sharedends.services.authz;

import mongooses.core.shared.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.services.authz.spi.impl.AuthorizationServiceProviderImplBase;
import webfx.framework.services.authz.spi.impl.UserPrincipalAuthorizationChecker;

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
