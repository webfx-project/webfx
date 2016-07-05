package naga.core.orm.entity;

/**
 * @author Bruno Salmon
 */
public class EntityId {

    private final Object domainClassId;
    private final Object primaryKey;

    EntityId(Object domainClassId, Object primaryKey) {
        this.domainClassId = domainClassId;
        this.primaryKey = primaryKey;
    }

    /**
     * @return a unique id identifying the domain class in the domain model
     */
    public Object getDomainClassId() {
        return domainClassId;
    }

    /**
     * @return the primary key of the counterpart database record
     */
    public Object getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityId entityId = (EntityId) o;

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
