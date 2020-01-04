package webfx.framework.shared.interceptors.dqlquery;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.services.datasource.LocalDataSourceService;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.spi.QueryServiceProvider;
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
        SingleServiceProvider.registerServiceInterceptor(QueryServiceProvider.class, target ->
                argument -> {
                    String language = argument.getLanguage();
                    Object dataSourceId = argument.getDataSourceId();
                    if (language != null && LocalDataSourceService.isDataSourceLocal(dataSourceId)) {
                        DataSourceModel dataSourceModel = DataSourceModelService.getDataSourceModel(dataSourceId);
                        if (dataSourceModel != null) {
                            String query = argument.getStatement();
                            String translatedQuery = dataSourceModel.translateQuery(language, query);
                            if (!query.equals(translatedQuery)) {
                                //Logger.log("Translated to: " + translatedQuery);
                                argument = QueryArgument.builder().copy(argument).setLanguage(null).setStatement(translatedQuery).build();
                            }
                        }
                    }
                    return target.executeQuery(argument);
                }
        );
    }
}
