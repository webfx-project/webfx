package webfx.framework.shared.interceptors.dqlquery;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.services.datasource.LocalDataSourceService;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.services.query.spi.QueryServiceProvider;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

/**
 * @author Bruno Salmon
 */
public class DqlQueryInterceptorModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-framework-shared-orm-dql-query-interceptor";
    }

    @Override
    public int getInitLevel() {
        return APPLICATION_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        // The purpose of this interceptor is to automatically translate DQL to SQL when the query reaches its local data source
        SingleServiceProvider.registerServiceInterceptor(QueryServiceProvider.class, targetProvider ->
                argument -> interceptAndExecuteQuery(argument, targetProvider)
        );
    }

    private Future<QueryResult> interceptAndExecuteQuery(QueryArgument argument, QueryServiceProvider targetProvider) {
        String language = argument.getLanguage();
        Object dataSourceId = argument.getDataSourceId();
        if (language != null && LocalDataSourceService.isDataSourceLocal(dataSourceId)) {
            DataSourceModel dataSourceModel = DataSourceModelService.getDataSourceModel(dataSourceId);
            if (dataSourceModel != null) {
                // Translating DQL to SQL
                String statement = argument.getStatement(); // can be DQL or SQL
                String sqlStatement = dataSourceModel.translateQuery(language, statement);
                if (!statement.equals(sqlStatement)) { // happens when DQL has been translated to SQL
                    //Logger.log("Translated to: " + sqlStatement);
                    argument = QueryArgument.builder().copy(argument).setLanguage(null).setStatement(sqlStatement).build();
                }
            }
        }
        return targetProvider.executeQuery(argument);
    }
}
