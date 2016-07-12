package naga.framework.orm.entity.impl;

import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public class EntityIdImpl implements EntityId {

    private final Object domainClassId;
    private final Object primaryKey;

    public EntityIdImpl(Object domainClassId, Object primaryKey) {
        if (domainClassId instanceof DomainClass)
            domainClassId = ((DomainClass) domainClassId).getModelId();
        this.domainClassId = domainClassId;
        this.primaryKey = primaryKey;
    }

    @Override
    public Object getDomainClassId() {
        return domainClassId;
    }

    @Override
    public Object getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public boolean isNew() {
        return primaryKey instanceof Integer && (Integer) primaryKey < 0; // temporary convention for new ids
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityIdImpl entityId = (EntityIdImpl) o;

        if (domainClassId != null ? !domainClassId.equals(entityId.domainClassId) : entityId.domainClassId != null) return false;
        return !(primaryKey != null ? !primaryKey.equals(entityId.primaryKey) : entityId.primaryKey != null);

    }

    @Override
    public String toString() {
        return "ID[" + domainClassId + ':' + primaryKey + ']';
    }

    @Override
    public int hashCode() {
        int result = domainClassId != null ? domainClassId.hashCode() : 0;
        result = 31 * result + (primaryKey != null ? primaryKey.hashCode() : 0);
        return result;
    }
}
