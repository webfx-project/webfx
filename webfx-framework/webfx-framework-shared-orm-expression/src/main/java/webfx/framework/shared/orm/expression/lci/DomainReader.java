package webfx.framework.shared.orm.expression.lci;

import webfx.extras.type.PrimType;

/**
 * Loose coupling interface used by expressions to read data (from domain objects and parameters).
 *
 * @param <T> The expected java class for domain objects.
 *
 * @author Bruno Salmon
 */
public interface DomainReader<T> {

    /**
     * Get a domain object from its identifier.
     *
     * @param id the domain object identifier
     * @return the domain object
     */
    T getDomainObjectFromId(Object id, Object src);

    /**
     * Get the identifier of the domain object.
     *
     * @param domainObject the domain object
     * @return the id of the domain object
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

    /**
     * Prepare the value just before it will be converted into the specified type. This method is called during
     * expression evaluation, so if the value is actually an instance of a domain object or an identifier, this method
     * can return the primary key instead of the instance itself. This allows to correctly evaluate expressions such as
     * object.id=10 or even object=10 when object.id and object are not numbers whereas the primary key is.
     *
     * @param value the value that will be converted
     * @param type the type into which the value will be converted
     * @return the value to be used for the actual conversion (such as the primary key if applicable or the unchanged value)
     */
    Object prepareValueBeforeTypeConversion(Object value, PrimType type);
}
