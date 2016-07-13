package naga.framework.orm.entity.resultset;

import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.resultset.impl.EntityChangesImpl;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Bruno Salmon
 */
public class EntityChangesBuilder {

    private EntityResultSetBuilder rsb;
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

    public EntityChangesBuilder addFieldChange(EntityId id, Object fieldId, Object fieldValue) {
        rsb().setFieldValue(id, fieldId, fieldValue);
        return this;
    }

    public EntityChangesBuilder removeFieldChange(EntityId id, Object fieldId) {
        rsb().unsetFieldValue(id, fieldId);
        return this;
    }

    public void clear() {
        rsb = null;
        deletedEntities = null;
    }

    private EntityResultSetBuilder rsb() {
        if (rsb == null)
            rsb = EntityResultSetBuilder.create();
        return rsb;
    }

    public EntityChanges build() {
        return new EntityChangesImpl(rsb == null ? null : rsb.build(), deletedEntities);
    }

    public static EntityChangesBuilder create() {
        return new EntityChangesBuilder();
    }

}
