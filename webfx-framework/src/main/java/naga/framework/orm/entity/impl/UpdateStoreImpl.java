package naga.framework.orm.entity.impl;

import naga.platform.services.log.Logger;
import naga.platform.services.update.UpdateService;
import naga.util.Arrays;
import naga.util.Objects;
import naga.util.async.Batch;
import naga.util.async.Future;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.orm.entity.result.*;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateResult;

/**
 * @author Bruno Salmon
 */
public class UpdateStoreImpl extends EntityStoreImpl implements UpdateStore {

    private final EntityChangesBuilder changesBuilder = EntityChangesBuilder.create();
    private EntityResultBuilder previousValues;

    public UpdateStoreImpl(DataSourceModel dataSourceModel) {
        super(dataSourceModel);
    }

    public UpdateStoreImpl(EntityStore underlyingStore) {
        super(underlyingStore);
    }

    @Override
    public EntityChanges getEntityChanges() {
        return changesBuilder.build();
    }

    @Override
    public <E extends Entity> E insertEntity(DomainClass domainClass) {
        E entity = createEntity(domainClass);
        changesBuilder.addInsertedEntityId(entity.getId());
        return entity;
    }

    @Override
    public <E extends Entity> E updateEntity(EntityId entityId) {
        changesBuilder.addUpdatedEntityId(entityId);
        return createEntity(entityId);
    }

    boolean updateEntity(EntityId id, Object domainFieldId, Object value, Object previousValue) {
        if (!Objects.areEquals(value, previousValue) && changesBuilder.hasEntityId(id)) {
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
            previousValues = EntityResultBuilder.create();
        previousValues.setFieldValue(id, domainFieldId, value);
    }

    void restorePreviousValues() {
        if (previousValues != null) {
            EntityResult rs = previousValues.build();
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
            Logger.log("Executing update batch " + Arrays.toStringWithLineFeeds(batch.getArray()));
            return UpdateService.executeUpdateBatch(batch).compose((ar, finalFuture) -> {
                markChangesAsCommitted();
                updateBatchGenerator.applyGeneratedKeys(ar, this);
                finalFuture.complete(ar);
            });
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
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
        changesBuilder.clear();
        restorePreviousValues();
    }

    @Override
    public void markChangesAsCommitted() {
        previousValues = null;
        changesBuilder.clear();
    }
}
