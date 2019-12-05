package webfx.framework.client.orm.reactive.dql.query;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import webfx.framework.client.activity.impl.elementals.activeproperty.HasActiveProperty;
import webfx.framework.client.orm.dql.DqlStatement;
import webfx.framework.client.orm.reactive.call.ReactiveCall;
import webfx.framework.client.orm.reactive.call.query.ReactiveQueryCall;
import webfx.framework.client.orm.reactive.dql.statement.ReactiveDqlStatement;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;
import webfx.framework.shared.orm.expression.builder.ThreadLocalReferenceResolver;
import webfx.framework.shared.orm.expression.sqlcompiler.sql.SqlCompiled;
import webfx.framework.shared.orm.expression.terms.Alias;
import webfx.framework.shared.orm.expression.terms.As;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
public class ReactiveDqlQuery<E> implements HasDataSourceModel, HasActiveProperty, ReactiveDqlQueryAPI<E, ReactiveDqlQuery<E>> {

    private final ReactiveDqlStatement<E> reactiveDqlStatement;
    protected final ReactiveCall<QueryArgument, QueryResult> reactiveQueryCall;
    private Predicate<DqlStatement> shortcutFunction = this::emptyShortcutFunction;
    private DataSourceModel dataSourceModel;

    public ReactiveDqlQuery(ReactiveDqlStatement<E> reactiveDqlStatement) {
        this(reactiveDqlStatement, new ReactiveQueryCall());
    }

    public ReactiveDqlQuery(ReactiveDqlStatement<E> reactiveDqlStatement, ReactiveCall<QueryArgument, QueryResult> reactiveQueryCall) {
        this.reactiveDqlStatement = reactiveDqlStatement;
        this.reactiveQueryCall = reactiveQueryCall;
        reactiveDqlStatement.resultingDqlStatementProperty().addListener((observableValue, oldDqlStatement, newDqlStatement) -> updateQueryArgument(newDqlStatement));
    }

    @Override
    public ReactiveDqlQuery<E> getReactiveDqlQuery() {
        return this;
    }

    @Override
    public ReactiveDqlStatement<E> getReactiveDqlStatement() {
        return reactiveDqlStatement;
    }

    @Override
    public ReactiveDqlQuery<E> setParent(ReactiveDqlQueryAPI<?, ?> parent) {
        return this;
    }

    @Override
    public void setShortcutFunction(Predicate<DqlStatement> shortcutFunction) {
        this.shortcutFunction = shortcutFunction;
    }

    @Override
    public ReactiveDqlQuery<E> setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        return this;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }

    /*==================================================================================================================
      ============================================== Fluent API ========================================================
      ================================================================================================================*/

    @Override
    public ReactiveDqlQuery<E> bindActivePropertyTo(ObservableValue<Boolean> activeProperty) {
        reactiveQueryCall.activeProperty().bind(activeProperty);
        return this;
    }

    @Override
    public ReactiveDqlQuery<E> start() {
        reactiveQueryCall.start();
        return this;
    }

    @Override
    public ReactiveDqlQuery<E> stop() {
        reactiveQueryCall.stop();
        return this;
    }

    @Override
    public boolean isStarted() {
        return reactiveQueryCall.isStarted();
    }

    @Override
    public void refreshWhenActive() {
        reactiveQueryCall.refreshWhenReady(false);
    }

    @Override
    public final ObservableValue<QueryResult> resultProperty() {
        return reactiveQueryCall.resultProperty();
    }

    @Override
    public final BooleanProperty activeProperty() {
        return reactiveQueryCall.activeProperty();
    }

    @Override
    public void setActive(boolean active) {
        reactiveQueryCall.setActive(active);
    }

    protected boolean emptyShortcutFunction(DqlStatement dqlStatement) {
        return dqlStatement.isInherentlyEmpty();
    }

    private SqlCompiled sqlCompiled;

    @Override
    public SqlCompiled getSqlCompiled() {
        return sqlCompiled;
    }

    protected void updateQueryArgument(DqlStatement dqlStatement) {
        //Logger.log("ReactiveDqlQuery.updateQueryArgument()");
        // Shortcut: when the dql statement is inherently empty, we return an empty entity list immediately (no server call) - unless we are in push mode already registered on the server
        if (shortcutFunction != null && shortcutFunction.test(dqlStatement))
            return;
        // Otherwise we compile the final dql statement into sql
        sqlCompiled = getDomainModel().parseAndCompileSelect(dqlStatement.toStringSelect(), dqlStatement.getSelectParameterValues());
        // And extract the possible parameters
        ArrayList<String> parameterNames = sqlCompiled.getParameterNames();
        if (!Collections.isEmpty(parameterNames))
            Logger.log("ReactiveEntityFilter.parameterNames = " + parameterNames);
        Object[] parameterValues = dqlStatement.getSelectParameterValues() ; // Collections.isEmpty(parameterNames) ? null : Collections.map(parameterNames, name -> getStore().getParameterValue(name)).toArray();
        // Generating the query argument
        QueryArgument queryArgument = new QueryArgument(sqlCompiled.getSql(), parameterValues, getDataSourceId());
        // Skipping the server call if there is no difference in the parameters compared to the last call
        if (!isDifferentFromLastQuery(queryArgument/*, active, push, pushClientId*/)) {
            queryArgument = null;
            onSkippingQueryCallAsSameArgument();
        }
        //memorizeAsLastQuery(active, push, pushClientId);
        if (queryArgument != null) {
            onBeforeQueryCall();
            //Logger.log("Setting queryArgument = " + queryArgument);
            reactiveQueryCall.setArgument(queryArgument);
        }
    }

    protected void onSkippingQueryCallAsSameArgument() {
        Logger.log("No difference with previous query");
    }

    protected void onBeforeQueryCall() {
    }

    private boolean isDifferentFromLastQuery(QueryArgument queryArgument) {
        return reactiveQueryCall.hasArgumentChangedSinceLastCall(queryArgument);
    }

    public void executeParsingCode(Runnable parsingCode) {
        ThreadLocalReferenceResolver.executeCodeInvolvingReferenceResolver(parsingCode, getRootAliasReferenceResolver());
    }

    @Override
    public DomainClass getDomainClass() {
        return getDomainModel().getClass(getDomainClassId());
    }

    @Override
    public Object getDomainClassId() {
        return getReactiveDqlStatement().getDomainClassId();
    }

    private ReferenceResolver rootAliasReferenceResolver;

    @Override
    public ReferenceResolver getRootAliasReferenceResolver() {
        if (rootAliasReferenceResolver == null) {
            // Before parsing, we prepare a ReferenceResolver to resolve possible references to root aliases
            Map<String, Alias<?>> rootAliases = new HashMap<>();
            rootAliasReferenceResolver = rootAliases::get;
            DqlStatement baseStatement = getReactiveDqlStatement().getBaseStatement();
            if (baseStatement != null) { // Root aliases are stored in the baseStatement
                // The first possible root alias is the base statement alias. Ex: Event e => the alias "e" then acts in a
                // similar way as "this" in java because it refers to the current Event row in the select, so some
                // expressions such as sub queries may refer to it (ex: select count(1) from Booking where event=e)
                String alias = baseStatement.getAlias();
                if (alias != null) // when defined, we add an Alias expression that can be returned when resolving this alias
                    rootAliases.put(alias, new Alias<>(alias, getDomainClass()));
                // Other possible root aliases can be As expressions defined in the base filter fields, such as sub queries
                // If fields contains for example (select ...) as xxx -> then xxx can be referenced in expression columns
                String fields = baseStatement.getFields();
                if (fields != null && fields.contains(" as ")) { // quick skipping if fields doesn't contains " as "
                    executeParsingCode(() -> {
                        DomainModel domainModel = getDomainModel();
                        Object domainClassId = getDomainClassId();
                        for (Expression<?> field : domainModel.parseExpressionArray(fields, domainClassId).getExpressions()) {
                            if (field instanceof As) { // If a field is a As expression,
                                As<?> as = (As<?>) field;
                                // we add an Alias expression that can be returned when resolving this alias
                                rootAliases.put(as.getAlias(), new Alias<>(as.getAlias(), as.getType()));
                            }
                        }
                    });
                }
            }
        }
        return rootAliasReferenceResolver;
    }

    /*==================================================================================================================
      ======================================= Classic static factory API ===============================================
      ================================================================================================================*/

    public static <E> ReactiveDqlQuery<E> create(ReactiveDqlStatement<E> reactiveDqlStatement) {
        return new ReactiveDqlQuery<>(reactiveDqlStatement);
    }

    /*==================================================================================================================
      ==================================== Conventional static factory API =============================================
      ================================================================================================================*/

    public static <E> ReactiveDqlQuery<E> create(Object mixin, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return initializeReactiveDqlQuery(create(reactiveDqlStatement), mixin);
    }

    protected static <E, RDQ extends ReactiveDqlQuery<E>> RDQ initializeReactiveDqlQuery(RDQ instance, Object mixin) {
        if (mixin instanceof HasDataSourceModel)
            instance.setDataSourceModel(((HasDataSourceModel) mixin).getDataSourceModel());
        if (mixin instanceof HasActiveProperty)
            instance.bindActivePropertyTo(((HasActiveProperty) mixin).activeProperty());
        return instance;
    }
}
