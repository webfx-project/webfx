package webfx.framework.shared.orm.entity.impl;

import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.domainmodel.DomainClass;

/**
 * @author Bruno Salmon
 */
public final class EntityIdImpl implements EntityId {

    private final DomainClass domainClass;
    private final Object primaryKey;

    public EntityIdImpl(DomainClass domainClass, Object primaryKey) {
        this.domainClass = domainClass;
        this.primaryKey = primaryKey;
    }

    @Override
    public DomainClass getDomainClass() {
        return domainClass;
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

        if (domainClass != null ? !domainClass.equals(entityId.domainClass) : entityId.domainClass != null) return false;
        return !(primaryKey != null ? !primaryKey.equals(entityId.primaryKey) : entityId.primaryKey != null);

    }

    @Override
    public String toString() {
        return "ID[" + domainClass + ':' + primaryKey + ']';
    }

    @Override
    public int hashCode() {
        int result = domainClass != null ? domainClass.hashCode() : 0;
        result = 31 * result + (primaryKey != null ? primaryKey.hashCode() : 0);
        return result;
    }

    private static int newPk;

    public static EntityIdImpl create(DomainClass domainClassId) {
        return new EntityIdImpl(domainClassId, --newPk);
    }

    public static EntityIdImpl create(DomainClass domainClassId, Object primaryKey) {
        return new EntityIdImpl(domainClassId, primaryKey);
    }
}
