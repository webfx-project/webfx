package mongoose.authn;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.HasDataSourceModel;
import naga.framework.spi.authn.AuthenticationServiceProvider;
import naga.framework.spi.authn.UsernamePasswordCredentials;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryService;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class MongooseAuthenticationServiceProvider implements AuthenticationServiceProvider, HasDataSourceModel {

    private final DataSourceModel dataSourceModel;

    public MongooseAuthenticationServiceProvider() {
        this(DomainModelSnapshotLoader.getDataSourceModel());
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
        Object[] parameters = {1, usernamePasswordCredentials.getUsername(), usernamePasswordCredentials.getPassword()};
        SqlCompiled sqlCompiled = getDomainModel().compileSelect("select id,frontendAccount.id from Person where frontendAccount.(corporation=? and username=? and password=?) order by id limit 1", parameters);
        return QueryService.executeQuery(new QueryArgument(sqlCompiled.getSql(), parameters, getDataSourceId()))
                .compose(result -> result.getRowCount() != 1 ? Future.failedFuture("Wrong user or password")
                      : Future.succeededFuture(new MongooseUserPrincipal(result.getValue(0, 0), result.getValue(0, 1)))
                );
    }
}
