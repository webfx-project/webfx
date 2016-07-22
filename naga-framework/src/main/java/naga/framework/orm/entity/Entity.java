package naga.framework.orm.entity;

import naga.commons.util.Booleans;
import naga.commons.util.Dates;
import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.framework.expression.Expression;

import java.time.Instant;

/**
 * Interface used to interact with an entity = a domain object which can persist in the database. Behind it can be a
 * POJO if a java class exists for that domain class or just a DynamicEntity which just acts as a flexible field values
 * container.
 *
 * @author Bruno Salmon
 */
public interface Entity {

    /**
     * @return the unique entity identifier
     */
    EntityId getId();

    /**
     * @return the store that manages this entity
     */
    EntityStore getStore();

    /***
     * @param domainFieldId the domain field unique id in the domain model
     * @return the value currently stored in this entity field
     */
    Object getFieldValue(Object domainFieldId);

    /**
     * Return the field value as a boolean. If the type is not a boolean, this can result in runtime errors.
     */
    default Boolean getBooleanFieldValue(Object domainFieldId) { return Booleans.toBoolean(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getStringFieldValue(Object domainFieldId) { return Strings.toString(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a int. If the type is not a int, this can result in runtime errors.
     */
    default Integer getIntegerFieldValue(Object domainFieldId) { return Numbers.toInteger(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a long. If the type is not a long, this can result in runtime errors.
     */
    default Long getLongFieldValue(Object domainFieldId) { return Numbers.toLong(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a double. If the type is not a double, this can result in runtime errors.
     */
    default Double getDoubleFieldValue(Object domainFieldId) { return Numbers.toDouble(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as an instant. If the type is not an instant, this can result in runtime errors.
     */
    default Instant getInstantFieldValue(Object domainFieldId) { return Dates.toInstant(getFieldValue(domainFieldId)); }

    /**
     * Set the value of an entity field
     * @param domainFieldId the domain field unique id in the domain model
     * @param value the value to store in this entity field
     */
    void setFieldValue(Object domainFieldId, Object value);

    void setForeignField(Object foreignFieldId, Object foreignFieldValue);

    EntityId getForeignEntityId(Object foreignFieldId);

    default <E extends Entity> E getForeignEntity(Object foreignFieldId) {
        return getStore().getEntity(getForeignEntityId(foreignFieldId));
    }

    default Object evaluate(Expression expression) {
        return getStore().evaluateEntityExpression(this, expression);
    }

}
