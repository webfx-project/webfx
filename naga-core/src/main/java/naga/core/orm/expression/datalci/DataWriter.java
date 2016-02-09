package naga.core.orm.expression.datalci;

/**
 * Loose coupling interface used by expressions to write into domain objects and parameters.
 *
 * @author Bruno Salmon
 */
public interface DataWriter extends DataReader {

    /**
     * Set the field value of a domain object.
     *
     * @param domainObject the domain object
     * @param fieldId the field identifier
     * @param fieldValue the new field value
     */
    void setDomainFieldValue(Object domainObject, Object fieldId, Object fieldValue);

    /**
     * Set the value of a parameter.
     * @param name the parameter name
     * @param value the new parameter value
     */
    void setParameterValue(String name, Object value);

}
