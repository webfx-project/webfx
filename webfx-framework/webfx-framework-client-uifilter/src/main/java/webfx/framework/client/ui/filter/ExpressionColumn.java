package webfx.framework.client.ui.filter;

import webfx.framework.shared.expression.terms.ExpressionArray;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.FieldsGroup;
import webfx.framework.shared.util.formatter.Formatter;
import webfx.framework.shared.util.formatter.FormatterRegistry;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonArray;
import webfx.platform.shared.services.json.JsonObject;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.expression.Expression;
import webfx.extras.visual.VisualColumn;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public interface ExpressionColumn {

    /**
     * @return the original expression set for that column (might express a foreign object or just a value).
     */
    Expression getExpression();

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
    Expression getForeignColumns();

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

    Expression getApplicableCondition();

    /**
     * @return the expression to be used to evaluate all values of the display result set for that column. It is the
     * same original expression if it expresses just a value but if it expresses a foreign object, the foreign fields
     * declared in the foreign class to display such an entity will be used instead.
     */
    Expression getVisualExpression();

    /**
     * @return the formatter to apply after the expression has been evaluated.
     */
    Formatter getVisualFormatter();

    /**
     * @return the associated display column.
     */
    VisualColumn getVisualColumn();

    boolean isReadOnly();

    /**
     * In case this expression column has been created using an expression definition and not an expression instance,
     * this methods needs to be called before to parse the expression so getExpression() doesn't return null.
     *
     * @param domainModel the domain model
     * @param domainClassId the domain class id (can be null if the expression is not related to a domain class)
     */
    ExpressionColumn parseExpressionDefinitionIfNecessary(DomainModel domainModel, Object domainClassId);

    // Shortcut method for the case the domain class is not null
    default ExpressionColumn parseExpressionDefinitionIfNecessary(DomainClass domainClass) {
        return parseExpressionDefinitionIfNecessary(domainClass.getDomainModel(), domainClass.getId());
    };

    /*** Factory methods ***/

    static ExpressionColumn create(String jsonOrExpressionDefinition) {
        if (jsonOrExpressionDefinition.startsWith("{"))
            return create(Json.parseObject(jsonOrExpressionDefinition));
        return create(jsonOrExpressionDefinition, (Formatter) null);
    }

    static ExpressionColumn create(JsonObject json) {
        return create(json.getString("expression"), json);
    }

    static ExpressionColumn create(String expressionDefinition, String jsonOptions) {
        return create(expressionDefinition, Json.parseObject(jsonOptions));
    }

    static ExpressionColumn create(String expressionDefinition, JsonObject options) {
        return new ExpressionColumnImpl(expressionDefinition, null, options.get("label"), FormatterRegistry.getFormatter(options.getString("format")), null, options);
    }

    static ExpressionColumn create(String expressionDefinition, Formatter expressionFormatter) {
        return new ExpressionColumnImpl(expressionDefinition, null, null, expressionFormatter, null, null);
    }

    static ExpressionColumn create(Expression expression) {
        return create(expression, null);
    }

    static ExpressionColumn create(Expression expression, VisualColumn visualColumn) {
        return new ExpressionColumnImpl(null, expression, null, null, visualColumn, null);
    }

    static ExpressionColumn[] fromJsonArray(String array, DomainModel domainModel, Object domainClassId) {
        ExpressionColumn[] columns = fromJsonArray(Json.parseArray(array));
        for (ExpressionColumn column : columns)
            column.parseExpressionDefinitionIfNecessary(domainModel, domainClassId);
        return columns;
    }

    static ExpressionColumn[] fromJsonArray(String array) {
        return fromJsonArray(Json.parseArray(array));
    }

    static ExpressionColumn[] fromJsonArray(JsonArray array) {
        int n = array.size();
        ExpressionColumn[] expressionColumns = new ExpressionColumn[n];
        for (int i = 0; i < n; i++) {
            Object element = array.getElement(i);
            expressionColumns[i] = element instanceof JsonObject ? create((JsonObject) element) : create(element.toString());
        }
        return expressionColumns;
    }

    static ExpressionColumn[] fromExpressions(Expression[] columnExpressions) {
        columnExpressions = expandFieldsGroups(columnExpressions);
        ExpressionColumn[] expressionColumns = new ExpressionColumn[columnExpressions.length];
        int columnIndex = 0;
        for (Expression columnExpression : columnExpressions)
            expressionColumns[columnIndex++] = create(columnExpression);
        return expressionColumns;
    }

    static ExpressionArray toVisualExpressionArray(ExpressionColumn[] columns) {
        return new ExpressionArray(Arrays.stream(columns).map(ExpressionColumn::getVisualExpression).collect(Collectors.toList()));
    }

    static Expression[] expandFieldsGroups(Expression[] columnExpressions) {
        return Arrays.stream(columnExpressions).flatMap(ce -> Arrays.stream(expandFieldsGroup(ce))).toArray(Expression[]::new);
    }

    static Expression[] expandFieldsGroup(Expression columnExpression) {
        if (columnExpression instanceof FieldsGroup) {
            columnExpression = ((FieldsGroup) columnExpression).getExpression();
            if (columnExpression instanceof ExpressionArray)
                return expandFieldsGroups(((ExpressionArray) columnExpression).getExpressions());
        }
        return new Expression[]{columnExpression};
    }

    static ExpressionColumn[] fromExpressionsDefinition(String columnExpressionsDefinition, DomainModel domainModel, Object classId) {
        return fromExpressions(domainModel.parseExpressionArray(columnExpressionsDefinition, classId).getExpressions());
    }

    static ExpressionColumn[] fromJsonArrayOrExpressionsDefinition(String jsonOrDef, DomainModel domainModel, Object classId) {
        return (jsonOrDef.startsWith("[")) ?
            fromJsonArray(jsonOrDef, domainModel, classId)
            : fromExpressionsDefinition(jsonOrDef, domainModel, classId);
    }

    // Shortcut method (when class can't be null)
    static ExpressionColumn[] fromJsonArrayOrExpressionsDefinition(String jsonOrDef, DomainClass domainClass) {
        return fromJsonArrayOrExpressionsDefinition(jsonOrDef, domainClass.getDomainModel(), domainClass);
    }
}
