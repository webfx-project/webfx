package naga.framework.orm.entity.impl;

import naga.commons.util.Arrays;
import naga.commons.util.Objects;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityFactoryRegistry;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.orm.entity.resultset.*;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class UpdateStoreImpl extends EntityStoreImpl implements UpdateStore {

    private final EntityChangesBuilder changesBuilder = EntityChangesBuilder.create();
    private EntityResultSetBuilder previousValues;

    public UpdateStoreImpl(DataSourceModel dataSourceModel) {
        super(dataSourceModel);
        dataSourceModel.getDomainModel(); // Making sure the domain model is loaded in memory and entity factories are registered
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

    boolean updateEntity(EntityId id, Object domainFieldId, Object value, Object previousValue) {
        if (!Objects.areEquals(value, previousValue)) {
            boolean firstFieldChange = updateEntity(id, domainFieldId, value);
            if (firstFieldChange)
                rememberPreviousEntityFieldValue(id, domainFieldId, previousValue);
            return firstFieldChange;
        }
        return false;
    }

    boolean updateEntity(EntityId id, Object domainFieldId, Object value) {
        return changesBuilder.addFieldChange(id, domainFieldId, value);
    }

    void rememberPreviousEntityFieldValue(EntityId id, Object domainFieldId, Object value) {
        if (previousValues == null)
            previousValues = EntityResultSetBuilder.create();
        previousValues.setFieldValue(id, domainFieldId, value);
    }

    void restorePreviousValues() {
        if (previousValues != null) {
            EntityResultSet rs = previousValues.build();
            for (EntityId id : rs.getEntityIds()) {
                Entity entity = getEntity(id);
                for (Object fieldId : rs.getFieldIds(id))
                    entity.setFieldValue(fieldId, rs.getFieldValue(id, fieldId));
            }
            previousValues = null;
        }
    }

    @Override
    public Future<Batch<UpdateResult>> executeUpdate(UpdateArgument[] initialUpdates) {
        try {
            EntityChangesToUpdateBatchGenerator.BatchGenerator updateBatchGenerator = EntityChangesToUpdateBatchGenerator.createUpdateBatchGenerator(getEntityChanges(), dataSourceModel, initialUpdates);
            Batch<UpdateArgument> batch = updateBatchGenerator.generate();
            Platform.log("Executing update batch " + Arrays.toString(batch.getArray()));
            Future<Batch<UpdateResult>> next = Future.future();
            return Platform.getUpdateService().executeUpdateBatch(batch).compose(ar -> {
                markChangesAsCommitted();
                updateBatchGenerator.applyGeneratedKeys(ar);
                next.complete(ar);
            }, next);
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
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
    public boolean hasChanges() {
        return !changesBuilder.isEmpty();
    }

    @Override
    public void cancelChanges() {
        restorePreviousValues();
        changesBuilder.clear();
    }

    @Override
    public void markChangesAsCommitted() {
        previousValues = null;
        changesBuilder.clear();
    }
}
