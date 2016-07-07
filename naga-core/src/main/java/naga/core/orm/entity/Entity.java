package naga.core.orm.entity;

import naga.core.orm.expression.Expression;
import naga.core.util.Booleans;
import naga.core.util.Numbers;
import naga.core.util.Strings;

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
    default boolean getBooleanFieldValue(Object domainFieldId) { return Booleans.booleanValue(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a String. If the type is not a String, this can result in runtime errors.
     */
    default String getStringFieldValue(Object domainFieldId) { return Strings.stringValue(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a int. If the type is not a int, this can result in runtime errors.
     */
    default int getIntFieldValue(Object domainFieldId) { return Numbers.intValue(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a long. If the type is not a long, this can result in runtime errors.
     */
    default long getLongFieldValue(Object domainFieldId) { return Numbers.longValue(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a double. If the type is not a double, this can result in runtime errors.
     */
    default double getDoubleFieldValue(Object domainFieldId) { return Numbers.doubleValue(getFieldValue(domainFieldId)); }

    /**
     * Set the value of an entity field
     * @param domainFieldId the domain field unique id in the domain model
     * @param value the value to store in this entity field
     */
    void setFieldValue(Object domainFieldId, Object value);


    default Entity getForeignEntity(Object foreignFieldId) {
        return getStore().getEntity((EntityId) getFieldValue(foreignFieldId));
    }

    default Object evaluate(Expression expression) {
        return getStore().evaluateEntityExpression(this, expression);
    }

}
