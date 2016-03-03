package naga.core.orm.entity;

import naga.core.orm.expression.Expression;

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
    EntityID getId();

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
     * Set the value of an entity field
     * @param domainFieldId
     * @param value
     */
    void setFieldValue(Object domainFieldId, Object value);


    default Entity getForeignEntity(Object foreignFieldId) {
        return getStore().getEntity((EntityID) getFieldValue(foreignFieldId));
    }

    default Object evaluate(Expression expression) {
        return getStore().evaluateEntityExpression(this, expression);
    }

}
