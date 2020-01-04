package webfx.framework.shared.interceptors.dqlsubmit;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.services.datasource.LocalDataSourceService;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.services.submit.spi.SubmitServiceProvider;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class DqlSubmitInterceptorModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-framework-shared-orm-dql-submit-interceptor";
    }

    @Override
    public int getInitLevel() {
        return APPLICATION_INIT_LEVEL;
    }

    @Override
    public void initModule() {
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
                            DataSourceModel dataSourceModel = DataSourceModelService.getDataSourceModel(dataSourceId);
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
