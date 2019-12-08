package mongoose.client.services.authz;

import mongoose.shared.domainmodel.MongooseDataSourceModel;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.services.authz.spi.impl.AuthorizationServiceProviderBase;
import webfx.framework.shared.services.authz.spi.impl.UserPrincipalAuthorizationChecker;

/**
 * @author Bruno Salmon
 */
public final class MongooseAuthorizationServiceProvider extends AuthorizationServiceProviderBase {

    private final DataSourceModel dataSourceModel;

    public MongooseAuthorizationServiceProvider() {
        this(MongooseDataSourceModel.get());
    }

    public MongooseAuthorizationServiceProvider(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    protected UserPrincipalAuthorizationChecker createUserPrincipalAuthorizationChecker(Object userPrincipal) {
        return new MongooseInMemoryUserPrincipalAuthorizationChecker(userPrincipal, dataSourceModel);
    }
}
