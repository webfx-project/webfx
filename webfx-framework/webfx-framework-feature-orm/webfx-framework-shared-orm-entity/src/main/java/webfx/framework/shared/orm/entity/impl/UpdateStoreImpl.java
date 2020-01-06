package webfx.framework.shared.orm.entity.impl;

import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.framework.shared.orm.entity.result.*;
import webfx.platform.shared.datascope.DataScope;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.services.submit.SubmitService;
import webfx.platform.shared.services.submit.SubmitResult;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.Objects;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public final class UpdateStoreImpl extends EntityStoreImpl implements UpdateStore {

    private final EntityChangesBuilder changesBuilder = EntityChangesBuilder.create();
    private EntityResultBuilder previousValues;
    private DataScope submitScope;

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
            if (previousValues != null && Objects.areEquals(value, previousValues.getFieldValue(id, domainFieldId))) {
                changesBuilder.removeFieldChange(id, domainFieldId);
                return true;
            } else {
                boolean firstFieldChange = updateEntity(id, domainFieldId, value);
                if (firstFieldChange)
                    rememberPreviousEntityFieldValue(id, domainFieldId, previousValue);
                return firstFieldChange;
            }
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
    public void setSubmitScope(DataScope submitScope) {
        this.submitScope = submitScope;
    }

    @Override
    public Future<Batch<SubmitResult>> submitChanges(SubmitArgument... initialSubmits) {
        try {
            EntityChangesToSubmitBatchGenerator.BatchGenerator updateBatchGenerator = EntityChangesToSubmitBatchGenerator.createSubmitBatchGenerator(getEntityChanges(), dataSourceModel, submitScope, initialSubmits);
            Batch<SubmitArgument> batch = updateBatchGenerator.generate();
            Logger.log("Executing submit batch " + Arrays.toStringWithLineFeeds(batch.getArray()));
            return SubmitService.executeSubmitBatch(batch).compose((ar, finalFuture) -> {
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
