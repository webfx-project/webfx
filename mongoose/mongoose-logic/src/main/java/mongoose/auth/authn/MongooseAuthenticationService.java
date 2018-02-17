package mongoose.auth.authn;

import mongoose.auth.authz.MongooseUser;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.services.authn.AuthenticationService;
import naga.platform.services.authn.UsernamePasswordCredentials;
import naga.platform.services.authz.User;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.spi.QueryService;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class MongooseAuthenticationService implements AuthenticationService {

    private final DataSourceModel dataSourceModel;

    public MongooseAuthenticationService(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    public Future<User> authenticate(Object userCredentials) {
        if (!(userCredentials instanceof UsernamePasswordCredentials))
            return Future.failedFuture(new IllegalArgumentException("MongooseAuthenticationService requires a UsernamePasswordCredentials argument"));
        UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials) userCredentials;
        Object[] parameters = {1, usernamePasswordCredentials.getUsername(), usernamePasswordCredentials.getPassword()};
        SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect("select FrontendAccount where corporation=? and username=? and password=? limit 1", parameters);
        return QueryService.executeQuery(new QueryArgument(sqlCompiled.getSql(), parameters, dataSourceModel.getId())).compose(result -> {
            if (result.getRowCount() != 1)
                return Future.failedFuture("Wrong user or password");
            return Future.succeededFuture(new MongooseUser(result.getValue(0, 0)));
        });
    }
}
