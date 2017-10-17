package naga.framework.orm.entity;

import naga.util.async.Batch;
import naga.util.async.Future;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.impl.UpdateStoreImpl;
import naga.framework.orm.entity.resultset.EntityChanges;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;

/**
 * @author Bruno Salmon
 */
public interface UpdateStore extends EntityStore {

    default <E extends Entity> E insertEntity(Class<E> entityClass) {
        return insertEntity(EntityFactoryRegistry.getEntityDomainClassId(entityClass));
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