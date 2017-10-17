package mongoose.auth;

import naga.util.async.Future;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.services.auth.UsernamePasswordToken;
import naga.platform.services.auth.spi.AuthService;
import naga.platform.services.auth.spi.User;
import naga.platform.services.query.QueryArgument;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class MongooseAuth implements AuthService {

    private final DataSourceModel dataSourceModel;

    public MongooseAuth(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    public Future<User> authenticate(Object authInfo) {
        if (!(authInfo instanceof UsernamePasswordToken))
            return Future.failedFuture(new IllegalArgumentException("MongooseAuth requires a UsernamePasswordToken argument"));
        UsernamePasswordToken token = (UsernamePasswordToken) authInfo;
        Object[] parameters = {1, token.getUsername(), token.getPassword()};
        SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect("select FrontendAccount where corporation=? and username=? and password=? limit 1", parameters);
        return Platform.getQueryService().executeQuery(new QueryArgument(sqlCompiled.getSql(), parameters, dataSourceModel.getId())).compose(result -> {
            if (result.getRowCount() != 1)
                return Future.failedFuture("Wrong user or password");
            return Future.succeededFuture(new MongooseUser(result.getValue(0, 0)));
        });
    }
}
