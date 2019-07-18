package webfx.framework.shared.orm.entity.result;

import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.result.impl.EntityChangesImpl;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Bruno Salmon
 */
public final class EntityChangesBuilder {

    private EntityResultBuilder rsb;
    private Collection<EntityId> deletedEntities;

    private EntityChangesBuilder() {
    }

    public EntityChangesBuilder addDeletedEntityId(EntityId id) {
        if (deletedEntities == null)
            deletedEntities = new HashSet<>();
        deletedEntities.add(id);
        return this;
    }

    public EntityChangesBuilder addInsertedEntityId(EntityId id) {
        if (id.isNew())
            addFieldChange(id, null, null);
        return this;
    }

    public EntityChangesBuilder addUpdatedEntityId(EntityId id) {
        if (!id.isNew())
            addFieldChange(id, null, null);
        return this;
    }

    public boolean hasEntityId(EntityId id) {
        return rsb != null && rsb.hasEntityId(id);
    }

    public boolean addFieldChange(EntityId id, Object fieldId, Object fieldValue) {
        return rsb().setFieldValue(id, fieldId, fieldValue);
    }

    public EntityChangesBuilder removeFieldChange(EntityId id, Object fieldId) {
        if (rsb != null)
            rsb.unsetFieldValue(id, fieldId);
        return this;
    }

    public void clear() {
        rsb = null;
        deletedEntities = null;
    }

    public boolean isEmpty() {
        return deletedEntities == null && (rsb == null || rsb.isEmpty());
    }

    private EntityResultBuilder rsb() {
        if (rsb == null)
            rsb = EntityResultBuilder.create();
        return rsb;
    }

    public EntityChanges build() {
        return new EntityChangesImpl(rsb == null ? null : rsb.build(), deletedEntities);
    }

    public static EntityChangesBuilder create() {
        return new EntityChangesBuilder();
    }
}
