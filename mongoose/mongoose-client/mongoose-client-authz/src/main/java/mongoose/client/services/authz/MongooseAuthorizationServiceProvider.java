package mongoose.client.services.authz;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.services.authz.spi.impl.AuthorizationServiceProviderBase;
import webfx.framework.shared.services.authz.spi.impl.UserPrincipalAuthorizationChecker;
import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;

/**
 * @author Bruno Salmon
 */
public final class MongooseAuthorizationServiceProvider extends AuthorizationServiceProviderBase {

    private final DataSourceModel dataSourceModel;

    public MongooseAuthorizationServiceProvider() {
        this(DataSourceModelService.getDefaultDataSourceModel());
    }

    public MongooseAuthorizationServiceProvider(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    protected UserPrincipalAuthorizationChecker createUserPrincipalAuthorizationChecker(Object userPrincipal) {
        return new MongooseInMemoryUserPrincipalAuthorizationChecker(userPrincipal, dataSourceModel);
    }
}
