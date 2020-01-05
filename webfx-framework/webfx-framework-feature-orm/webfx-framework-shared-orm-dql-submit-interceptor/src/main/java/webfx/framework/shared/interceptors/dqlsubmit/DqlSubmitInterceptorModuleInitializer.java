package webfx.framework.shared.interceptors.dqlsubmit;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.DqlStatement;
import webfx.framework.shared.orm.expression.terms.Equals;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.framework.shared.orm.expression.terms.Update;
import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;
import webfx.platform.shared.datascope.SchemaScope;
import webfx.platform.shared.datascope.SchemaScopeBuilder;
import webfx.platform.shared.datascope.DataScope;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.services.datasource.LocalDataSourceService;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.services.submit.spi.SubmitServiceProvider;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        // The purpose of this interceptor is to automatically translate DQL to SQL and compute the schema scope when
        // the submit reaches its local data source (works only with DQL)
        SingleServiceProvider.registerServiceInterceptor(SubmitServiceProvider.class, targetProvider ->
                new SubmitServiceProvider() {

                    @Override
                    public Future<SubmitResult> executeSubmit(SubmitArgument argument) {
                        return interceptAndExecuteSubmit(argument, targetProvider);
                    }

                    @Override
                    public Future<Batch<SubmitResult>> executeSubmitBatch(Batch<SubmitArgument> batch) {
                        return interceptAndExecuteSubmitBatch(batch, targetProvider);
                    }

                });
    }

    private static Future<SubmitResult> interceptAndExecuteSubmit(SubmitArgument argument, SubmitServiceProvider targetProvider) {
        return targetProvider.executeSubmit(translateSubmit(argument));
    }

    private static Future<Batch<SubmitResult>> interceptAndExecuteSubmitBatch(Batch<SubmitArgument> batch, SubmitServiceProvider targetProvider) {
        return targetProvider.executeSubmitBatch(translateBatch(batch));
    }

    private static SubmitArgument translateSubmit(SubmitArgument argument) {
        String language = argument.getLanguage();
        Object dataSourceId = argument.getDataSourceId();
        if (language != null && LocalDataSourceService.isDataSourceLocal(dataSourceId)) {
            DataSourceModel dataSourceModel = DataSourceModelService.getDataSourceModel(dataSourceId);
            if (dataSourceModel != null) {
                String statement = argument.getStatement(); // can be DQL or SQL
                String sqlStatement = dataSourceModel.translateStatementIfDql(language, statement);
                if (!statement.equals(sqlStatement)) { // happens when DQL has been translated to SQL
                    //Logger.log("Translated to: " + sqlStatement);
                    argument = SubmitArgument.builder().copy(argument)
                            .setLanguage(null).setStatement(sqlStatement)
                            .setSchemaScope(createSchemaScope(statement, dataSourceModel))
                            .build();
                }
            }
        }
        return argument;
    }

    private static Batch<SubmitArgument> translateBatch(Batch<SubmitArgument> batch) {
        return new Batch<>(Arrays.stream(batch.getArray()).map(DqlSubmitInterceptorModuleInitializer::translateSubmit).toArray(SubmitArgument[]::new));
    }

    private static DataScope createSchemaScope(String dqlSubmit, DataSourceModel dataSourceModel) {
        return new DataScope() { // returning a Scope wrapper so the scope computation can be skipped when not necessary (ie if intersects method is never called)
            private SchemaScope computedScope;
            @Override
            public boolean intersects(DataScope dataScope) {
                if (computedScope == null) {
                    // TODO Should we cache this (dqlStatement => modified fields)?
                    DqlStatement<Object> dqlStatement = dataSourceModel.parseStatement(dqlSubmit);
                    SchemaScopeBuilder ssb = SchemaScope.builder();
                    if (dqlStatement instanceof Update) { // Update => only fields in set clause are impacted
                        List<DomainField> modifiedFields = collectModifiedFields(((Update<Object>) dqlStatement).getSetClause());
                        modifiedFields.forEach(domainField -> ssb.addField(domainField.getDomainClass().getId(), domainField.getId()));
                    } else { // Insert or Delete => all fields are impacted
                        DomainClass domainClass = dqlStatement.getDomainClass() instanceof DomainClass ? (DomainClass) dqlStatement.getDomainClass()
                                : dataSourceModel.getDomainModel().getClass(dqlStatement.getDomainClass());
                        ssb.addClass(domainClass.getId());
                    }
                    computedScope = ssb.build();
                }
                return computedScope.intersects(dataScope);
            }
        };
    }

    private static List<DomainField> collectModifiedFields(ExpressionArray<?> modifyingExpressions) {
        List<DomainField> modifiedFields = new ArrayList<>();
        for (Expression<?> expression : modifyingExpressions.getExpressions()) {
            if (expression instanceof Equals) {
                Expression<?> left = ((Equals<?>) expression).getLeft();
                if (left instanceof DomainField)
                    modifiedFields.add((DomainField) left);
            }
        }
        return modifiedFields;
    }

}
