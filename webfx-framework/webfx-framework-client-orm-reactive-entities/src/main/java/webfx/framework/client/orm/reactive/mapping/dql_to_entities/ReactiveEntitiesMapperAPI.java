package webfx.framework.client.orm.reactive.mapping.dql_to_entities;

import javafx.collections.ObservableList;
import webfx.framework.client.orm.reactive.dql.query.ReactiveDqlQuery;
import webfx.framework.client.orm.reactive.dql.query.ReactiveDqlQueryAPI;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.HasEntityStore;
import webfx.platform.shared.util.async.Handler;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public interface ReactiveEntitiesMapperAPI<E extends Entity, THIS> extends HasEntityStore, ReactiveDqlQueryAPI<E, THIS> {

    ReactiveEntitiesMapper<E> getReactiveEntitiesMapper();

    @Override
    default DataSourceModel getDataSourceModel() {
        return getReactiveDqlQuery().getDataSourceModel();
    }

    default EntityStore getStore() {
        return getReactiveEntitiesMapper().getStore();
    }

    default ReactiveDqlQuery<E> getReactiveDqlQuery() {
        return getReactiveEntitiesMapper().getReactiveDqlQuery();
    }

    default EntityList<E> getEntities() {
        return getReactiveEntitiesMapper().getEntities();
    }

    default EntityList<E> getCurrentEntities() {
        return getReactiveEntitiesMapper().getCurrentEntities();
    }

    default ObservableList<E> getObservableEntities() {
        return getReactiveEntitiesMapper().getObservableEntities();
    }

    default void refreshWhenActive() {
        getReactiveEntitiesMapper().refreshWhenActive();
    }

    default THIS setStore(EntityStore store) {
        getReactiveEntitiesMapper().setStore(store);
        return (THIS) this;
    }

    default THIS setListId(Object listId) {
        getReactiveEntitiesMapper().setListId(listId);
        return (THIS) this;
    }

    default THIS setRestrictedFilterList(List<E> restrictedFilterList) {
        getReactiveEntitiesMapper().setRestrictedFilterList(restrictedFilterList);
        return (THIS) this;
    }

    default THIS addEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        getReactiveEntitiesMapper().addEntitiesHandler(entitiesHandler);
        return (THIS) this;
    }

    default THIS removeEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        getReactiveEntitiesMapper().removeEntitiesHandler(entitiesHandler);
        return (THIS) this;
    }
}
