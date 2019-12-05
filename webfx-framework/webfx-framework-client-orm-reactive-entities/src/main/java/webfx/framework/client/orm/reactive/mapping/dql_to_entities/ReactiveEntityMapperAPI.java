package webfx.framework.client.orm.reactive.mapping.dql_to_entities;

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
public interface ReactiveEntityMapperAPI<E extends Entity, THIS> extends HasEntityStore, ReactiveDqlQueryAPI<E, THIS> {

    ReactiveEntityMapper<E> getReactiveEntityMapper();

    @Override
    default DataSourceModel getDataSourceModel() {
        return getReactiveDqlQuery().getDataSourceModel();
    }

    default EntityStore getStore() {
        return getReactiveEntityMapper().getStore();
    }

    default ReactiveDqlQuery<E> getReactiveDqlQuery() {
        return getReactiveEntityMapper().getReactiveDqlQuery();
    }

    default EntityList<E> getEntityList() {
        return getReactiveEntityMapper().getEntityList();
    }

    default EntityList<E> getCurrentEntityList() {
        return getReactiveEntityMapper().getCurrentEntityList();
    }

    default void refreshWhenActive() {
        getReactiveEntityMapper().refreshWhenActive();
    }

    default THIS setStore(EntityStore store) {
        getReactiveEntityMapper().setStore(store);
        return (THIS) this;
    }

    default THIS setListId(Object listId) {
        getReactiveEntityMapper().setListId(listId);
        return (THIS) this;
    }

    default THIS setRestrictedFilterList(List<E> restrictedFilterList) {
        getReactiveEntityMapper().setRestrictedFilterList(restrictedFilterList);
        return (THIS) this;
    }

    default THIS addEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        getReactiveEntityMapper().addEntitiesHandler(entitiesHandler);
        return (THIS) this;
    }

    default THIS removeEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        getReactiveEntityMapper().removeEntitiesHandler(entitiesHandler);
        return (THIS) this;
    }
}
