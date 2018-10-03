package webfx.framework.shared.orm.entity;

import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.Dates;
import webfx.platform.shared.util.Numbers;
import webfx.platform.shared.util.Strings;
import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.orm.domainmodel.DomainClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
     * @return the domain class for the entity
     */
    default DomainClass getDomainClass() {
        return getId().getDomainClass();
    }

    /**
     * @return the primary key of the counterpart database record for this entity
     */
    default Object getPrimaryKey() {
        return getId().getPrimaryKey();
    }

    /**
     * @return true if the designated entity is not yet inserted in the database. In this case, the primary key is a
     * temporary but non null object that works to identify the in-memory newly created entity instance.
     */
    default boolean isNew() {
        return getId().isNew();
    }

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
     * Return the field value as an Instant. If the type is not an instant, this can result in runtime errors.
     */
    default Instant getInstantFieldValue(Object domainFieldId) { return Dates.toInstant(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a LocalDateTime. If the type is not an instant, this can result in runtime errors.
     */
    default LocalDateTime getLocalDateTimeFieldValue(Object domainFieldId) { return Dates.toLocalDateTime(getFieldValue(domainFieldId)); }

    /**
     * Return the field value as a LocalDate. If the type is not an instant, this can result in runtime errors.
     */
    default LocalDate getLocalDateFieldValue(Object domainFieldId) { return Dates.toLocalDate(getFieldValue(domainFieldId)); }

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

    default Object evaluate(String expression) {
        return getStore().evaluateEntityExpression(this, expression);
    }

    default <E extends Entity> Object evaluate(Expression<E> expression) {
        return getStore().evaluateEntityExpression((E) this, expression);
    }

    default <E extends Entity> void setExpressionValue(Expression<E> expression, Object value) {
        getStore().setEntityExpressionValue((E) this, expression, value);
    }
}
