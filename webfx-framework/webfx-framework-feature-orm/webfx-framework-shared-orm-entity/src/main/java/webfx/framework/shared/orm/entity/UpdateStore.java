package webfx.framework.shared.orm.entity;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.entity.impl.UpdateStoreImpl;
import webfx.framework.shared.orm.entity.result.EntityChanges;
import webfx.platform.shared.datascope.DataScope;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

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

    void setSubmitScope(DataScope submitScope);

    Future<Batch<SubmitResult>> submitChanges(SubmitArgument... initialSubmits);

    // Factory

    static UpdateStore create(DataSourceModel dataSourceModel) {
        return new UpdateStoreImpl(dataSourceModel);
    }

    static UpdateStore createAbove(EntityStore underlyingStore) {
        return new UpdateStoreImpl(underlyingStore);
    }
}