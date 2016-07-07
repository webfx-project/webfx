package naga.core.orm.entity.resultset.impl;

import naga.core.orm.entity.EntityId;
import naga.core.orm.entity.resultset.EntityChanges;
import naga.core.orm.entity.resultset.EntityResultSet;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public class EntityChangesImpl implements EntityChanges {

    private final EntityResultSet insertedUpdatedEntities;
    private final Collection<EntityId> deletedEntities;

    public EntityChangesImpl(EntityResultSet insertedUpdatedEntities, Collection<EntityId> deletedEntities) {
        this.insertedUpdatedEntities = insertedUpdatedEntities;
        this.deletedEntities = deletedEntities;
    }

    @Override
    public EntityResultSet getInsertedUpdatedEntityResultSet() {
        return insertedUpdatedEntities;
    }

    @Override
    public Collection<EntityId> getDeletedEntityIds() {
        return deletedEntities;
    }
}
