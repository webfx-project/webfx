package naga.core.orm.entity;

import naga.core.orm.entity.impl.EntityIdImpl;

/**
 * Interface for a unique identifier designating an entity. An EntityId doesn't require it's designated Entity to be
 * also present in memory.
 *
 * @author Bruno Salmon
 */
public interface EntityId {

    /**
     * @return a unique id identifying the entity domain class in the domain model
     */
    Object getDomainClassId();

    /**
     * @return the primary key of the counterpart database record for this entity
     */
    Object getPrimaryKey();

    /**
     * @return true is the designated entity is not yet inserted in the database. In this case, the primary key is a
     * temporary but non null object that works to identify the in-memory newly created entity instance.
     */
    boolean isNew();

    static EntityId create(Object domainClassId, Object primaryKey) {
        return new EntityIdImpl(domainClassId, primaryKey);
    }
}
