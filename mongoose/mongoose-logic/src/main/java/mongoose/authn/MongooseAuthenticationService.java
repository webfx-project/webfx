package mongoose.authn;

import mongoose.domainmodel.loader.DomainModelSnapshotLoader;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.spi.authn.AuthenticationService;
import naga.framework.spi.authn.UsernamePasswordCredentials;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.spi.QueryService;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class MongooseAuthenticationService implements AuthenticationService {

    private final DataSourceModel dataSourceModel;

    public MongooseAuthenticationService() {
        this(DomainModelSnapshotLoader.getDataSourceModel());
    }

    public MongooseAuthenticationService(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    public Future<MongooseUserPrincipal> authenticate(Object userCredentials) {
        if (!(userCredentials instanceof UsernamePasswordCredentials))
            return Future.failedFuture(new IllegalArgumentException("MongooseAuthenticationService requires a UsernamePasswordCredentials argument"));
        UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials) userCredentials;
        Object[] parameters = {1, usernamePasswordCredentials.getUsername(), usernamePasswordCredentials.getPassword()};
        SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect("select FrontendAccount where corporation=? and username=? and password=? limit 1", parameters);
        return QueryService.executeQuery(new QueryArgument(sqlCompiled.getSql(), parameters, dataSourceModel.getId())).compose(result -> {
            if (result.getRowCount() != 1)
                return Future.failedFuture("Wrong user or password");
            return Future.succeededFuture(new MongooseUserPrincipal(result.getValue(0, 0)));
        });
    }
}
