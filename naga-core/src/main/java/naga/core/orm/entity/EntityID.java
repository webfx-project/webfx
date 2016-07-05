package naga.core.orm.entity;

/**
 * @author Bruno Salmon
 */
public interface EntityId {

    /**
     * @return a unique id identifying the domain class in the domain model
     */
    Object getDomainClassId();

    /**
     * @return the primary key of the counterpart database record
     */
    Object getPrimaryKey();
}
