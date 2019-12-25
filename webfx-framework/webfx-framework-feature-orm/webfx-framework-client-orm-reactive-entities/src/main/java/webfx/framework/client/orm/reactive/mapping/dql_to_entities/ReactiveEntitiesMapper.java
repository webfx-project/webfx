package webfx.framework.client.orm.reactive.mapping.dql_to_entities;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import webfx.framework.client.orm.reactive.dql.query.ReactiveDqlQuery;
import webfx.framework.client.orm.reactive.dql.querypush.ReactiveDqlQueryPush;
import webfx.framework.shared.orm.dql.sqlcompiler.sql.SqlCompiled;
import webfx.framework.shared.orm.entity.*;
import webfx.framework.shared.orm.entity.query_result_to_entities.QueryResultToEntitiesMapper;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.async.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class ReactiveEntitiesMapper<E extends Entity> implements HasEntityStore, ReactiveEntitiesMapperAPI<E, ReactiveEntitiesMapper<E>> {

    private final ReactiveDqlQuery<E> reactiveDqlQuery;
    private final ObjectProperty<EntityList<E>> entitiesProperty = new SimpleObjectProperty<EntityList<E>/*GWT*/>() {
        @Override
        protected void invalidated() {
            scheduleOnEntitiesChanged();
        }
    };
    private ObservableList<E> observableEntities;
    private List<E> restrictedFilterList;
    private EntityStore store;
    private List<Handler<EntityList<E>>> entitiesHandlers = new ArrayList<>();
    private static int mapperCount = 0;
    private Object listId = "reactiveEntitiesMapper-" + ++mapperCount;

    public ReactiveEntitiesMapper(ReactiveDqlQuery<E> reactiveDqlQuery) {
        this.reactiveDqlQuery = reactiveDqlQuery;
        Properties.runOnPropertiesChange(p -> onNewQueryResult((QueryResult) p.getValue()), reactiveDqlQuery.resultProperty());
    }

    @Override
    public ReactiveEntitiesMapper<E> getReactiveEntitiesMapper() {
        return this;
    }

    public ReactiveEntitiesMapper<E> setStore(EntityStore store) {
        this.store = store;
        return this;
    }

    @Override
    public final EntityStore getStore() {
        // If not store has been explicitly set, we implicitly create a new one
        if (store == null)
            setStore(EntityStore.create(getDataSourceModel()));
        return store;
    }

    public ReactiveEntitiesMapper<E> setListId(Object listId) {
        this.listId = listId;
        return this;
    }

    public ReactiveEntitiesMapper<E> setRestrictedFilterList(List<E> restrictedFilterList) {
        this.restrictedFilterList = restrictedFilterList;
        return this;
    }

    public ReactiveEntitiesMapper<E> addEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        entitiesHandlers.add(entitiesHandler);
        return this;
    }

    public ReactiveEntitiesMapper<E> removeEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
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
        //Logger.log("ReactiveEntitiesMapper received queryResult with " + queryResult.getRowCount() + " rows");
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
        if (entities == getEntities()) // It's also important to make sure the output instance is not the same
            entities = new EntityListWrapper<>(entities); // by wrapping the list (for entitiesToVisualResults() cache system)
        return lastEntitiesOutput = entities;
    }

    private boolean onEntitiesChangedScheduled;

    private void scheduleOnEntitiesChanged() {
        if (!onEntitiesChangedScheduled) {
            onEntitiesChangedScheduled = true;
            Platform.runLater(() -> {
                onEntitiesChangedScheduled = false;
                if (reactiveDqlQuery.isStarted())
                    onEntitiesChanged();
            });
        }
    }

    private void onEntitiesChanged() {
        //Logger.log("ReactiveEntitiesMapper.onEntityListChanged()");
        EntityList<E> entities = getEntities();
        //entitiesHandlers.forEach(handler -> handler.handle(entities));
        for (int i = 0; i < entitiesHandlers.size(); i++)
            entitiesHandlers.get(i).handle(entities);
    }

    public EntityList<E> getEntities() {
        return entitiesProperty.get();
    }

    public EntityList<E> getCurrentEntities() {
        return getStore().getOrCreateEntityList(listId);
    }

    @Override
    public ObservableList<E> getObservableEntities() {
        if (observableEntities == null) {
            observableEntities = new OptimizedObservableListWrapper<>();
            addEntitiesHandler(entities -> observableEntities.setAll(entities));
        }
        return observableEntities;
    }

    /*==================================================================================================================
      ======================================= Classic static factory API ===============================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveEntitiesMapper<E> create(ReactiveDqlQuery<E> reactiveDqlQuery) {
        return new ReactiveEntitiesMapper<>(reactiveDqlQuery);
    }

    /*==================================================================================================================
      ========================== Shortcut static factory API (ReactiveDqlStatement) ====================================
      ================================================================================================================*/

    public static <E extends Entity> ReactiveEntitiesMapper<E> createReactiveChain() {
        return create(ReactiveDqlQuery.createReactiveChain());
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createReactiveChain(Object mixin) {
        return create(ReactiveDqlQuery.createReactiveChain(mixin));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createPushReactiveChain() {
        return create(ReactiveDqlQueryPush.createReactiveChain());
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createPushReactiveChain(Object mixin) {
        return create(ReactiveDqlQueryPush.createReactiveChain(mixin));
    }
    
    // Master

    public static <E extends Entity> ReactiveEntitiesMapper<E> createMasterReactiveChain(Object pm) {
        return create(ReactiveDqlQuery.createMasterReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createMasterReactiveChain(Object mixin, Object pm) {
        return create(ReactiveDqlQuery.createMasterReactiveChain(mixin, pm));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createMasterPushReactiveChain(Object pm) {
        return create(ReactiveDqlQueryPush.createMasterReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createMasterPushReactiveChain(Object mixin, Object pm) {
        return create(ReactiveDqlQueryPush.createMasterReactiveChain(mixin, pm));
    }
    
    // Group

    public static <E extends Entity> ReactiveEntitiesMapper<E> createGroupReactiveChain(Object pm) {
        return create(ReactiveDqlQuery.createGroupReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createGroupReactiveChain(Object mixin, Object pm) {
        return create(ReactiveDqlQuery.createGroupReactiveChain(mixin, pm));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createGroupPushReactiveChain(Object pm) {
        return create(ReactiveDqlQueryPush.createGroupReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createGroupPushReactiveChain(Object mixin, Object pm) {
        return create(ReactiveDqlQueryPush.createGroupReactiveChain(mixin, pm));
    }

    // Slave

    public static <E extends Entity> ReactiveEntitiesMapper<E> createSlaveReactiveChain(Object pm) {
        return create(ReactiveDqlQuery.createSlaveReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createSlaveReactiveChain(Object mixin, Object pm) {
        return create(ReactiveDqlQuery.createSlaveReactiveChain(mixin, pm));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createSlavePushReactiveChain(Object pm) {
        return create(ReactiveDqlQueryPush.createSlaveReactiveChain(pm));
    }

    public static <E extends Entity> ReactiveEntitiesMapper<E> createSlavePushReactiveChain(Object mixin, Object pm) {
        return create(ReactiveDqlQueryPush.createSlaveReactiveChain(mixin, pm));
    }
}
