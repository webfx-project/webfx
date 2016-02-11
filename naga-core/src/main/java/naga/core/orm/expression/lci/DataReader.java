package naga.core.orm.expression.lci;

/**
 * Loose coupling interface used by expressions to read data (from domain objects and parameters).
 *
 * @param <T> The expected java class for domain objects.
 *
 * @author Bruno Salmon
 */
public interface DataReader<T> {

    /**
     * Get a domain object from its identifier.
     *
     * @param id the domain object identifier
     * @return the domain object
     */
    T getDomainObjectFromId(Object id);

    /**
     * Get the identifier of the domain object.
     *
     * @param domainObject
     * @return
     */
    Object getDomainObjectId(T domainObject);

    /**
     * Get the field value of a domain object.
     *
     * @param domainObject the domain object to get the field value from
     * @param fieldId the field identifier
     * @return the field value
     */
    Object getDomainFieldValue(T domainObject, Object fieldId);

    /**
     * Get the value of a parameter.
     *
     * @param name the parameter name
     * @return the parameter value
     */
    Object getParameterValue(String name);
}
