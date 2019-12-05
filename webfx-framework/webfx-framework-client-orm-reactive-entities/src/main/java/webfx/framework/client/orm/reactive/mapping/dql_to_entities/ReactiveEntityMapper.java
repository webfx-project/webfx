package webfx.framework.client.orm.reactive.mapping.dql_to_entities;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import webfx.framework.client.orm.reactive.dql.query.ReactiveDqlQuery;
import webfx.framework.client.orm.reactive.dql.querypush.ReactiveDqlQueryPush;
import webfx.framework.client.orm.reactive.dql.statement.ReactiveDqlStatement;
import webfx.framework.shared.orm.entity.*;
import webfx.framework.shared.orm.entity.query_result_to_entities.QueryResultToEntitiesMapper;
import webfx.framework.shared.orm.expression.sqlcompiler.sql.SqlCompiled;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.async.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class ReactiveEntityMapper<E extends Entity> implements HasEntityStore, ReactiveEntityMapperAPI<E, ReactiveEntityMapper<E>> {

    private final ReactiveDqlQuery<E> reactiveDqlQuery;
    protected final ObjectProperty<EntityList<E>> entitiesProperty = new SimpleObjectProperty<EntityList<E>/*GWT*/>() {
        @Override
        protected void invalidated() {
            scheduleOnEntityListChanged();
        }
    };
    private List<E> restrictedFilterList;
    private EntityStore store;
    protected List<Handler<EntityList<E>>> entitiesHandlers = new ArrayList<>();
    private static int mapperCount = 0;
    protected Object listId = "reactiveEntityMapper-" + ++mapperCount;

    public ReactiveEntityMapper(ReactiveDqlQuery<E> reactiveDqlQuery) {
        this.reactiveDqlQuery = reactiveDqlQuery;
        Properties.runOnPropertiesChange(p -> onNewQueryResult((QueryResult) p.getValue()), reactiveDqlQuery.resultProperty());
    }

    @Override
    public ReactiveEntityMapper<E> getReactiveEntityMapper() {
        return this;
    }

    public ReactiveEntityMapper<E> setStore(EntityStore store) {
        this.store = store;
        return this;
    }

    @Override
    public final EntityStore getStore() {
        // If not set, we create a new store
        if (store == null)
            setStore(EntityStore.create(reactiveDqlQuery.getDataSourceModel()));
        return store;
    }

    public ReactiveEntityMapper<E> setListId(Object listId) {
        this.listId = listId;
        return this;
    }

    public ReactiveEntityMapper<E> setRestrictedFilterList(List<E> restrictedFilterList) {
        this.restrictedFilterList = restrictedFilterList;
        return this;
    }

    public ReactiveEntityMapper<E> addEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        entitiesHandlers.add(entitiesHandler);
        return this;
    }

    public ReactiveEntityMapper<E> removeEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        entitiesHandlers.remove(entitiesHandler);
        return this;
    }

    public ReactiveDqlQuery<E> getReactiveDqlQuery() {
        return reactiveDqlQuery;
    }

    public void refreshWhenActive() {
        reactiveDqlQuery.refreshWhenActive();
    }

    private void onNewQueryResult(QueryResult queryResult) {
        //Logger.log("ReactiveEntityMapper.onNewQueryResult()");
        entitiesProperty.set(queryResultToEntities(queryResult));
    }

    // Cache fields used in queryResultToEntities() method
    private QueryResult lastRsInput;
    private EntityList<E> lastEntitiesOutput;

    private EntityList<E> queryResultToEntities(QueryResult rs) {
        // Returning the cached output if input didn't change (ex: the same result set instance is emitted again on active property change)
        if (rs == lastRsInput)
            return lastEntitiesOutput;
        // Otherwise really generates the entity list (the content will changed but not the instance of the returned list)
        SqlCompiled sqlCompiled = reactiveDqlQuery.getSqlCompiled();
        EntityList<E> entities = QueryResultToEntitiesMapper.mapQueryResultToEntities(rs, sqlCompiled.getQueryMapping(), getStore(), listId);
        // Caching and returning the result
        lastRsInput = rs;
        if (entities == getEntityList()) // It's also important to make sure the output instance is not the same
            entities = new EntityListWrapper<>(entities); // by wrapping the list (for entitiesToVisualResults() cache system)
        return lastEntitiesOutput = entities;
    }

    private boolean onEntityListChangedScheduled;

    protected void scheduleOnEntityListChanged() {
        if (!onEntityListChangedScheduled) {
            onEntityListChangedScheduled = true;
            Platform.runLater(() -> {
                onEntityListChangedScheduled = false;
                if (reactiveDqlQuery.isStarted())
                    onEntityListChanged();
            });
        }
    }

    protected void onEntityListChanged() {
        //Logger.log("ReactiveEntityMapper.onEntityListChanged()");
        EntityList<E> entityList = getEntityList();
        //entitiesHandlers.forEach(handler -> handler.handle(entityList));
        for (int i = 0; i < entitiesHandlers.size(); i++)
            entitiesHandlers.get(i).handle(entityList);
    }

    public EntityList<E> getEntityList() {
        return entitiesProperty.get();
    }

    public EntityList<E> getCurrentEntityList() {
        return getStore().getOrCreateEntityList(listId);
    }


    /*==================================================================================================================
      ======================================= Classic static factory API ===============================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveEntityMapper<E> create(ReactiveDqlQuery<E> reactiveDqlQuery) {
        return new ReactiveEntityMapper<>(reactiveDqlQuery);
    }

    /*==================================================================================================================
      ========================== Shortcut static factory API (ReactiveDqlStatement) ====================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveEntityMapper<E> create(ReactiveDqlStatement<E> reactiveDqlStatement) {
        return create(ReactiveDqlQuery.create(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveEntityMapper<E> create(Object mixin, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return create(ReactiveDqlQuery.create(mixin, reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveEntityMapper<E> createPush(ReactiveDqlStatement<E> reactiveDqlStatement) {
        return create(ReactiveDqlQueryPush.create(reactiveDqlStatement));
    }

    public static <E extends Entity> ReactiveEntityMapper<E> createPushReactiveChain(Object mixin) {
        return create(ReactiveDqlQueryPush.create(mixin, ReactiveDqlStatement.create()));
    }

    public static <E extends Entity> ReactiveEntityMapper<E> createPush(Object mixin, ReactiveDqlStatement<E> reactiveDqlStatement) {
        return create(ReactiveDqlQueryPush.create(mixin, reactiveDqlStatement));
    }

}
