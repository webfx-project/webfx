package mongoose.client.services.authn;

import mongoose.shared.domainmodel.MongooseDataSourceModel;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;
import webfx.framework.shared.services.authn.UsernamePasswordCredentials;
import webfx.framework.shared.services.authn.spi.AuthenticationServiceProvider;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryService;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public final class MongooseAuthenticationServiceProvider implements AuthenticationServiceProvider, HasDataSourceModel {

    private final DataSourceModel dataSourceModel;

    public MongooseAuthenticationServiceProvider() {
        this(MongooseDataSourceModel.get());
    }

    public MongooseAuthenticationServiceProvider(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }

    @Override
    public Future<MongooseUserPrincipal> authenticate(Object userCredentials) {
        if (!(userCredentials instanceof UsernamePasswordCredentials))
            return Future.failedFuture(new IllegalArgumentException("MongooseAuthenticationServiceProvider requires a UsernamePasswordCredentials argument"));
        UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials) userCredentials;
        return QueryService.executeQuery(new QueryArgument(getDataSourceId(), "DQL", "select id,frontendAccount.id from Person where frontendAccount.(corporation=? and username=? and password=?) order by id limit 1", 1, usernamePasswordCredentials.getUsername(), usernamePasswordCredentials.getPassword()))
                .compose(result -> result.getRowCount() != 1 ? Future.failedFuture("Wrong user or password")
                      : Future.succeededFuture(new MongooseUserPrincipal(result.getValue(0, 0), result.getValue(0, 1)))
                );
    }
}
