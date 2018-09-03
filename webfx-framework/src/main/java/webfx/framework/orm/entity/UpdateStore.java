package webfx.framework.orm.entity;

import webfx.platforms.core.util.async.Batch;
import webfx.platforms.core.util.async.Future;
import webfx.framework.orm.domainmodel.DataSourceModel;
import webfx.framework.orm.domainmodel.DomainClass;
import webfx.framework.orm.entity.impl.UpdateStoreImpl;
import webfx.framework.orm.entity.result.EntityChanges;
import webfx.platforms.core.services.update.UpdateArgument;
import webfx.platforms.core.services.update.UpdateResult;

/**
 * @author Bruno Salmon
 */
public interface UpdateStore extends EntityStore {

    default <E extends Entity> E insertEntity(Class<E> entityClass) {
        return insertEntity(EntityDomainClassIdRegistry.getEntityDomainClassId(entityClass));
    }

    default <E extends Entity> E insertEntity(Object domainClassId) {
        return insertEntity(getDomainClass(domainClassId));
    }

    <E extends Entity> E insertEntity(DomainClass domainClass);

    default <E extends Entity> E updateEntity(E entity) {
        updateEntity(entity.getId());
        return copyEntity(entity);
    }

    <E extends Entity> E updateEntity(EntityId entityId);

    default void deleteEntity(Entity entity) {
        deleteEntity(entity.getId());
    }

    void deleteEntity(EntityId entityId);

    EntityChanges getEntityChanges();

    boolean hasChanges();

    void cancelChanges();

    void markChangesAsCommitted();

    default Future<Batch<UpdateResult>> executeUpdate() {
        return executeUpdate(null);
    }

    Future<Batch<UpdateResult>> executeUpdate(UpdateArgument[] initialUpdates);

    // Factory

    static UpdateStore create(DataSourceModel dataSourceModel) {
        return new UpdateStoreImpl(dataSourceModel);
    }

    static UpdateStore createAbove(EntityStore underlyingStore) {
        return new UpdateStoreImpl(underlyingStore);
    }
}