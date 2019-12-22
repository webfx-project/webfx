package webfx.framework.client.orm.reactive.mapping.entities_to_grid;

import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.domainmodel.formatter.ValueFormatter;

/**
 * @author Bruno Salmon
 */
public interface EntityColumn<E extends Entity> {

    /**
     * @return the original expression set for that column (might express a foreign object or just a value).
     */
    Expression<E> getExpression();

    /**
     * @return the foreign class if the original expression expresses a foreign object or null if it expresses just a value
     */
    DomainClass getForeignClass();

    String getForeignAlias();

    /**
     * @return the foreign columns to be used to display the foreign object, or null if the original expression is just a value.
     * These columns are generally those defined in the domain class, but it's possible to override them when the column
     * is parsed from json using the "foreignColumns" key. Ex: {expression: 'myEntity', foreignColumns: '[icon,name]'}
     */
    Expression<?> getForeignColumns();

    /**
     * @return the foreign condition to be used when selecting a new foreign object, or null if the original expression is just a value.
     * This condition is generally the one defined in the foreign field, but it's possible to override it when the column
     * is parsed from json using the "foreignCondition" key. Ex: {expression: 'myEntity', foreignWhere: '!cancelled'}
     */
    String getForeignWhere();

    String getForeignOrderBy();

    /**
     * @return the foreign search condition to be used when selecting a new foreign object, or null if the original expression is just a value.
     * This search condition is generally the one defined in the domain class, but it's possible to override it when the column
     * is parsed from json using the "foreignFields" key. Ex: {expression: 'myEntity', foreignSearchCondition: 'name like ?searchLike'}
     */
    String getForeignSearchCondition();

    Expression<E> getApplicableCondition();

    /**
     * @return the expression to be used to evaluate all values of the display result set for that column. It is the
     * same original expression if it expresses just a value but if it expresses a foreign object, the foreign fields
     * declared in the foreign class to display such an entity will be used instead.
     */
    Expression<E> getDisplayExpression();

    /**
     * @return the formatter to apply after the expression has been evaluated.
     */
    ValueFormatter getDisplayFormatter();

    String getName();

    boolean isVisible();

    boolean isReadOnly();

    /**
     * In case this expression column has been created using an expression definition and not an expression instance,
     * this methods needs to be called before to parse the expression so getExpression() doesn't return null.
     *
     * @param domainModel the domain model
     * @param domainClassId the domain class id (can be null if the expression is not related to a domain class)
     */
    EntityColumn<E> parseExpressionDefinitionIfNecessary(DomainModel domainModel, Object domainClassId);

    // Shortcut method for the case the domain class is not null
    default EntityColumn<E> parseExpressionDefinitionIfNecessary(DomainClass domainClass) {
        return parseExpressionDefinitionIfNecessary(domainClass.getDomainModel(), domainClass.getId());
    }

}
