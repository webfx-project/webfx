package naga.framework.orm.entity.impl;

import naga.commons.util.Arrays;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityFactoryRegistry;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.orm.entity.resultset.EntityChanges;
import naga.framework.orm.entity.resultset.EntityChangesBuilder;
import naga.framework.orm.entity.resultset.EntityChangesToUpdateBatchGenerator;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class UpdateStoreImpl extends EntityStoreImpl implements UpdateStore {

    private final EntityChangesBuilder changesBuilder = EntityChangesBuilder.create();

    public UpdateStoreImpl(DataSourceModel dataSourceModel) {
        super(dataSourceModel);
    }

    EntityChangesBuilder getChangesBuilder() {
        return changesBuilder;
    }

    @Override
    public EntityChanges getEntityChanges() {
        return changesBuilder.build();
    }

    @Override
    public <E extends Entity> E insertEntity(Class<E> entityClass) {
        return insertEntity(EntityFactoryRegistry.getEntityDomainClassId(entityClass));
    }

    @Override
    public <E extends Entity> E insertEntity(Object domainClassId) {
        return insertEntity(getDomainClass(domainClassId));
    }

    @Override
    public <E extends Entity> E insertEntity(DomainClass domainClass) {
        EntityId newId = EntityId.create(domainClass);
        changesBuilder.addInsertedEntityId(newId);
        return createEntity(newId);
    }

    void updateEntity(EntityId id, Object domainFieldId, Object value) {
        changesBuilder.addFieldChange(id, domainFieldId, value);
    }

    @Override
    public Future<Batch<UpdateResult>> executeUpdate() {
        Batch<UpdateArgument> batch = EntityChangesToUpdateBatchGenerator.generateUpdateBatch(getEntityChanges(), dataSourceModel);
        Platform.log("Executing update batch " + Arrays.toString(batch.getArray()));
        return Platform.getUpdateService().executeUpdateBatch(batch);
    }

    @Override
    public void deleteEntity(Entity entity) {
        if (entity != null)
            deleteEntity(entity.getId());
    }

    @Override
    public void deleteEntity(EntityId entityId) {
        changesBuilder.addDeletedEntityId(entityId);
    }

    @Override
    public void cancelChanges() {
        changesBuilder.clear();
    }
}
