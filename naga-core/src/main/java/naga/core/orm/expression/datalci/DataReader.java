package naga.core.orm.expression.datalci;

/**
 * Loose coupling interface used by expressions to read domain objects and parameters.
 *
 * @author Bruno Salmon
 */

public interface DataReader {

    /**
     * Get a domain object from its identifier.
     *
     * @param id the domain object identifier
     * @return the domain object
     */
    Object getDomainObjectFromId(Object id);

    /**
     * Get the identifier of the domain object.
     *
     * @param domainObject
     * @return
     */
    Object getDomainObjectId(Object domainObject);

    /**
     * Get the field value of a domain object.
     *
     * @param domainObject the domain object to get the field value from
     * @param fieldId the field identifier
     * @return the field value
     */
    Object getDomainFieldValue(Object domainObject, Object fieldId);

    /**
     * Get the value of a parameter.
     *
     * @param name the parameter name
     * @return the parameter value
     */
    Object getParameterValue(String name);

}
