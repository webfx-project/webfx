package webfx.framework.shared.interceptors.dqlsubmit;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainField;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.*;
import webfx.framework.shared.services.datasourcemodel.DataSourceModelService;
import webfx.platform.shared.datascope.DataScope;
import webfx.platform.shared.datascope.KeyDataScope;
import webfx.platform.shared.datascope.MultiKeyDataScope;
import webfx.platform.shared.datascope.aggregate.AggregateScope;
import webfx.platform.shared.datascope.aggregate.AggregateScopeBuilder;
import webfx.platform.shared.datascope.schema.SchemaScope;
import webfx.platform.shared.datascope.schema.SchemaScopeBuilder;
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
                            .addDataScope(createDataScope(statement, dataSourceModel, argument.getParameters()))
                            .build();
                }
            }
        }
        return argument;
    }

    private static Batch<SubmitArgument> translateBatch(Batch<SubmitArgument> batch) {
        return new Batch<>(Arrays.stream(batch.getArray()).map(DqlSubmitInterceptorModuleInitializer::translateSubmit).toArray(SubmitArgument[]::new));
    }

    private static DataScope createDataScope(String dqlSubmitStatement, DataSourceModel dataSourceModel, Object[] parameters) {
        // Returning a wrapper so the scope computation can be skipped if not used later
        // (ex: if intersects method is never called or submit fails)
        return new MultiKeyDataScope() {

            private KeyDataScope[] keyDataScopes;

            @Override
            public KeyDataScope[] getKeyDataScopes() { // Called only if get used
                if (keyDataScopes == null) {
                    DqlStatement<Object> dqlStatement = dataSourceModel.parseStatement(dqlSubmitStatement);
                    // Building the schema and aggregate scope
                    SchemaScopeBuilder ssb = SchemaScope.builder();
                    AggregateScopeBuilder asb = AggregateScope.builder();
                    if (dqlStatement instanceof Update) { // Update => only fields in set clause are impacted
                        for (Expression<?> expression : ((Update<Object>) dqlStatement).getSetClause().getExpressions()) {
                            if (expression instanceof Equals) {
                                Equals<?> equals = (Equals<?>) expression;
                                Expression<?> left = equals.getLeft();
                                if (left instanceof DomainField) {
                                    DomainField domainField = (DomainField) left;
                                    ssb.addField(domainField.getDomainClass().getId(), domainField.getId());
                                    DomainClass foreignClass = domainField.getForeignClass();
                                    if (foreignClass != null && foreignClass.isAggregate()) {
                                        Expression<?> right = equals.getRight();
                                        Object value = null;
                                        if (right instanceof Constant)
                                            value = ((Constant<?>) right).getConstantValue();
                                        else if (right instanceof Parameter)
                                            value = parameters[0]; // TODO compute the correct parameter index
                                        if (value != null)
                                            asb.addAggregate(foreignClass.getName(), value);
                                    }
                                }
                            }
                        }
                    } else { // Insert or Delete => all fields are impacted
                        DomainClass domainClass = dqlStatement.getDomainClass() instanceof DomainClass ? (DomainClass) dqlStatement.getDomainClass()
                                : dataSourceModel.getDomainModel().getClass(dqlStatement.getDomainClass());
                        ssb.addClass(domainClass.getId());
                    }
                    SchemaScope schemaScope = ssb.build();
                    AggregateScope aggregateScope = asb.build();
                    // Putting the scopes into the array
                    keyDataScopes = new KeyDataScope[] { schemaScope, aggregateScope };
                }
                return keyDataScopes;
            }
        };
    }
}
