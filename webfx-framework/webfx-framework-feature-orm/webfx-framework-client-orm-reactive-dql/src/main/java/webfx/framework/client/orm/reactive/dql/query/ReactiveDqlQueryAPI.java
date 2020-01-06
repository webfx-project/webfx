package webfx.framework.client.orm.reactive.dql.query;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import webfx.framework.client.activity.impl.elementals.activeproperty.HasActiveProperty;
import webfx.framework.client.orm.reactive.dql.statement.ReactiveDqlStatement;
import webfx.framework.client.orm.reactive.dql.statement.ReactiveDqlStatementAPI;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlCompiled;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;
import webfx.platform.shared.datascope.aggregate.AggregateScope;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public interface ReactiveDqlQueryAPI<E, THIS> extends HasDataSourceModel, HasActiveProperty, ReactiveDqlStatementAPI<E, THIS> {

    ReactiveDqlQuery<E> getReactiveDqlQuery();

    default ReactiveDqlStatement<E> getReactiveDqlStatement() {
        return getReactiveDqlQuery().getReactiveDqlStatement();
    }

    default THIS setActiveParent(ReactiveDqlQueryAPI<?, ?> activeParent) {
        getReactiveDqlQuery().setActiveParent(activeParent);
        return (THIS) this;
    }

    @Override
    default DataSourceModel getDataSourceModel() {
        return getReactiveDqlQuery().getDataSourceModel();
    }

    default boolean isStarted() {
        return getReactiveDqlQuery().isStarted();
    }

    @Override
    default BooleanProperty activeProperty() {
        return getReactiveDqlQuery().activeProperty();
    }

    default void setActive(boolean active) {
        getReactiveDqlQuery().setActive(active);
    }

    default void refreshWhenActive() {
        getReactiveDqlQuery().refreshWhenActive();
    }

    default ObservableValue<QueryResult> resultProperty() {
        return getReactiveDqlQuery().resultProperty();
    }

    default SqlCompiled getSqlCompiled() {
        return getReactiveDqlQuery().getSqlCompiled();
    }

    default void executeParsingCode(Runnable parsingCode) {
        getReactiveDqlQuery().executeParsingCode(parsingCode);
    }

    default DomainClass getDomainClass() {
        return getReactiveDqlQuery().getDomainClass();
    }

    default Object getDomainClassId() {
        return getReactiveDqlQuery().getDomainClassId();
    }

    default ReferenceResolver getRootAliasReferenceResolver() {
        return getReactiveDqlQuery().getRootAliasReferenceResolver();
    }

    /*==================================================================================================================
      ============================================== Fluent API ========================================================
      ================================================================================================================*/

    default <T> THIS setAggregateScope(ObservableValue<T> property, Converter<T, AggregateScope> toAggregateScopeConverter) {
        getReactiveDqlQuery().setAggregateScope(property, toAggregateScopeConverter);
        return (THIS) this;
    }

    default THIS setDataSourceModel(DataSourceModel dataSourceModel) {
        getReactiveDqlQuery().setDataSourceModel(dataSourceModel);
        dataSourceModel.getDomainModel();
        return (THIS) this;
    }

    default THIS bindActivePropertyTo(ObservableValue<Boolean> activeProperty) {
        getReactiveDqlQuery().bindActivePropertyTo(activeProperty);
        return (THIS) this;
    }

    default THIS unbindActiveProperty() {
        getReactiveDqlQuery().unbindActiveProperty();
        return (THIS) this;
    }

    default THIS start() {
        getReactiveDqlQuery().start();
        return (THIS) this;
    }

    default THIS stop() {
        getReactiveDqlQuery().stop();
        return (THIS) this;
    }
}
