package webfx.framework.shared.services.datasourcemodel;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.services.datasourcemodel.spi.DataSourceModelProvider;
import webfx.platform.shared.services.datasource.LocalDataSourceService;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.spi.QueryServiceProvider;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.services.submit.spi.SubmitServiceProvider;
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

    /* Installing DQL to SQL translator interceptor for query and submit services
     * TODO Move this into a separate module when the build tool will provide the automatic conditional module inclusion
     * TODO (here: when both webfx-platform-shared-query and webfx-platform-shared-domainmodel are included)
     * TODO (and same with webfx-platform-shared-submit and webfx-platform-shared-domainmodel)
     */
    static {
        SingleServiceProvider.registerServiceInterceptor(QueryServiceProvider.class, target ->
                argument -> {
                    String language = argument.getLanguage();
                    Object dataSourceId = argument.getDataSourceId();
                    if (language != null && LocalDataSourceService.isDataSourceLocal(dataSourceId)) {
                        DataSourceModel dataSourceModel = getDataSourceModel(dataSourceId);
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

        SingleServiceProvider.registerServiceInterceptor(SubmitServiceProvider.class, target ->
                new SubmitServiceProvider() {

                    @Override
                    public Future<SubmitResult> executeSubmit(SubmitArgument argument) {
                        return target.executeSubmit(translateSubmit(argument));
                    }

                    @Override
                    public Future<Batch<SubmitResult>> executeSubmitBatch(Batch<SubmitArgument> batch) {
                        return target.executeSubmitBatch(translateBatch(batch));
                    }

                    private SubmitArgument translateSubmit(SubmitArgument argument) {
                        String submitLang = argument.getLanguage();
                        Object dataSourceId = argument.getDataSourceId();
                        if (submitLang != null && LocalDataSourceService.isDataSourceLocal(dataSourceId)) {
                            DataSourceModel dataSourceModel = getDataSourceModel(dataSourceId);
                            if (dataSourceModel != null) {
                                String submitString = argument.getStatement();
                                String translatedSubmit = dataSourceModel.translateStatementIfDql(argument.getLanguage(), submitString);
                                if (!submitString.equals(translatedSubmit)) {
                                    //Logger.log("Translated to: " + translatedSubmit);
                                    argument = SubmitArgument.builder().copy(argument).setLanguage(null).setStatement(translatedSubmit).build();
                                }
                            }
                        }
                        return argument;
                    }

                    private Batch<SubmitArgument> translateBatch(Batch<SubmitArgument> batch) {
                        return new Batch<>(Arrays.stream(batch.getArray()).map(this::translateSubmit).toArray(SubmitArgument[]::new));
                    }
                });
    }
}
