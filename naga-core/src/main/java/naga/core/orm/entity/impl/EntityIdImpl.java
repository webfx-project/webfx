package naga.core.orm.entity.impl;

import naga.core.orm.entity.EntityId;

/**
 * @author Bruno Salmon
 */
public class EntityIdImpl implements EntityId {

    private final Object domainClassId;
    private final Object primaryKey;

    EntityIdImpl(Object domainClassId, Object primaryKey) {
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
