package naga.framework.orm.entity.result.impl;

import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.result.EntityChanges;
import naga.framework.orm.entity.result.EntityResult;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class EntityChangesImpl implements EntityChanges {

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
