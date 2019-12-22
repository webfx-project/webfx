package webfx.framework.shared.orm.entity.result.impl;

import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.result.EntityChanges;
import webfx.framework.shared.orm.entity.result.EntityResult;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class EntityChangesImpl implements EntityChanges {

    private final EntityResult insertedUpdatedEntities;
    private final Collection<EntityId> deletedEntities;

    public EntityChangesImpl(EntityResult insertedUpdatedEntities, Collection<EntityId> deletedEntities) {
        this.insertedUpdatedEntities = insertedUpdatedEntities;
        this.deletedEntities = deletedEntities;
    }

    @Override
    public EntityResult getInsertedUpdatedEntityResult() {
        return insertedUpdatedEntities;
    }

    @Override
    public Collection<EntityId> getDeletedEntityIds() {
        return deletedEntities;
    }
}
