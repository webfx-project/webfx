package webfx.framework.shared.services.datasourcemodel;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.services.datasourcemodel.spi.DataSourceModelProvider;
import webfx.platform.shared.services.datasource.LocalDataSourceService;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.spi.QueryServiceProvider;
import webfx.platform.shared.services.update.UpdateArgument;
import webfx.platform.shared.services.update.UpdateResult;
import webfx.platform.shared.services.update.spi.UpdateServiceProvider;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.Arrays;
import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class DataSourceModelService {

    public static DataSourceModelProvider getProvider() {
        return SingleServiceProvider.getProvider(DataSourceModelProvider.class, () -> ServiceLoader.load(DataSourceModelProvider.class));
    }

    public static Object getDefaultDataSourceId() {
        return getProvider().getDefaultDataSourceId();
    }

    public static DataSourceModel getDefaultDataSourceModel() {
        return getProvider().getDefaultDataSourceModel();
    }

    public static Future<DataSourceModel> loadDataSourceModel(Object dataSourceId) {
        return getProvider().loadDataSourceModel(dataSourceId);
    }

    public static DataSourceModel getDataSourceModel(Object dataSourceId) {
        return getProvider().getDataSourceModel(dataSourceId);
    }

    /* Installing DQL to SQL translator interceptor for query and update services
     * TODO Move this into a separate module when the build tool will provide the automatic conditional module inclusion
     * TODO (here: when both webfx-platform-shared-query and webfx-platform-shared-domainmodel are included)
     * TODO (and same with webfx-platform-shared-update and webfx-platform-shared-domainmodel)
     */
    static {
        SingleServiceProvider.registerServiceInterceptor(QueryServiceProvider.class, target ->
                argument -> {
                    String queryLang = argument.getQueryLang();
                    Object dataSourceId = argument.getDataSourceId();
                    if (queryLang != null && LocalDataSourceService.isDataSourceLocal(dataSourceId)) {
                        DataSourceModel dataSourceModel = getDataSourceModel(dataSourceId);
                        if (dataSourceModel != null) {
                            String queryString = argument.getQueryString();
                            String translatedQuery = dataSourceModel.translateQuery(queryLang, queryString);
                            if (!queryString.equals(translatedQuery)) {
                                //Logger.log("Translated to: " + translatedQuery);
                                argument = new QueryArgument(argument, translatedQuery);
                            }
                        }
                    }
                    return target.executeQuery(argument);
                }
        );

        SingleServiceProvider.registerServiceInterceptor(UpdateServiceProvider.class, target ->
                new UpdateServiceProvider() {

                    @Override
                    public Future<UpdateResult> executeUpdate(UpdateArgument argument) {
                        return target.executeUpdate(translateUpdate(argument));
                    }

                    @Override
                    public Future<Batch<UpdateResult>> executeUpdateBatch(Batch<UpdateArgument> batch) {
                        return target.executeUpdateBatch(translateBatch(batch));
                    }

                    private UpdateArgument translateUpdate(UpdateArgument argument) {
                        String updateLang = argument.getUpdateLang();
                        Object dataSourceId = argument.getDataSourceId();
                        if (updateLang != null && LocalDataSourceService.isDataSourceLocal(dataSourceId)) {
                            DataSourceModel dataSourceModel = getDataSourceModel(dataSourceId);
                            if (dataSourceModel != null) {
                                String updateString = argument.getUpdateString();
                                String translatedUpdate = dataSourceModel.translateStatementIfDql(argument.getUpdateLang(), updateString);
                                if (!updateString.equals(translatedUpdate)) {
                                    //Logger.log("Translated to: " + translatedUpdate);
                                    argument = new UpdateArgument(argument, translatedUpdate);
                                }
                            }
                        }
                        return argument;
                    }

                    private Batch<UpdateArgument> translateBatch(Batch<UpdateArgument> batch) {
                        return new Batch<>(Arrays.stream(batch.getArray()).map(this::translateUpdate).toArray(UpdateArgument[]::new));
                    }
                });
    }
}
