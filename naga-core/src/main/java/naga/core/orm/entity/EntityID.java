package naga.core.orm.entity;

/**
 * @author Bruno Salmon
 */
public class EntityID {

    private final Object domainClassId;
    private final Object primaryKey;

    EntityID(Object domainClassId, Object primaryKey) {
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

        EntityID entityID = (EntityID) o;

        if (domainClassId != null ? !domainClassId.equals(entityID.domainClassId) : entityID.domainClassId != null) return false;
        return !(primaryKey != null ? !primaryKey.equals(entityID.primaryKey) : entityID.primaryKey != null);

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
