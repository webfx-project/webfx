package webfx.framework.ui.filter;

import webfx.framework.orm.domainmodel.DomainClass;
import webfx.framework.ui.formatter.Formatter;
import webfx.framework.ui.formatter.FormatterRegistry;
import webfx.platform.services.json.Json;
import webfx.platform.services.json.JsonArray;
import webfx.platform.services.json.JsonObject;
import webfx.framework.orm.domainmodel.DomainModel;
import webfx.framework.expression.Expression;
import webfx.fxdata.displaydata.DisplayColumn;

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

    /**
     * @return the foreign fields to be used to display the foreign object, or null if the original expression is just a value.
     * These fields are generally those defined in the domain class, but it's possible to override them when the column
     * is parsed from json using the "foreignFields" key. Ex: {expression: 'myEntity', foreignFields: '[icon,name]'}
     */
    Expression getForeignFields();

    /**
     * @return the foreign condition to be used when selecting a new foreign object, or null if the original expression is just a value.
     * This condition is generally the one defined in the foreign field, but it's possible to override it when the column
     * is parsed from json using the "foreignCondition" key. Ex: {expression: 'myEntity', foreignCondition: '!cancelled'}
     */
    String getForeignCondition();

    /**
     * @return the foreign search condition to be used when selecting a new foreign object, or null if the original expression is just a value.
     * This search condition is generally the one defined in the domain class, but it's possible to override it when the column
     * is parsed from json using the "foreignFields" key. Ex: {expression: 'myEntity', foreignSearchCondition: 'name like ?searchLike'}
     */
    String getForeignSearchCondition();

    /**
     * @return the expression to be used to evaluate all values of the display result set for that column. It is the
     * same original expression if it expresses just a value but if it expresses a foreign object, the foreign fields
     * declared in the foreign class to display such an entity will be used instead.
     */
    Expression getDisplayExpression();

    /**
     * @return the formatter to apply after the expression has been evaluated.
     */
    Formatter getDisplayFormatter();

    /**
     * @return the associated display column.
     */
    DisplayColumn getDisplayColumn();

    /**
     * In case this expression column has been created using an expression definition and not an expression instance,
     * this methods needs to be called before to parse the expression so getExpression() doesn't return null.
     *
     * @param domainModel the domain model
     * @param domainClassId the domain class id
     */
    ExpressionColumn parseExpressionDefinitionIfNecessary(DomainModel domainModel, Object domainClassId);


    /*** Factory methods ***/

    static ExpressionColumn create(String jsonOrExpressionDefinition) {
        if (jsonOrExpressionDefinition.startsWith("{"))
            return create(Json.parseObject(jsonOrExpressionDefinition));
        return new ExpressionColumnImpl(jsonOrExpressionDefinition, null, null, null, null, null);
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

    static ExpressionColumn create(Expression expression, DisplayColumn displayColumn) {
        return new ExpressionColumnImpl(null, expression, null, null, displayColumn, null);
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
        ExpressionColumn[] expressionColumns = new ExpressionColumn[columnExpressions.length];
        int columnIndex = 0;
        for (Expression columnExpression : columnExpressions)
            expressionColumns[columnIndex++] = ExpressionColumn.create(columnExpression);
        return expressionColumns;
    }

    static ExpressionColumn[] fromExpressionsDefinition(String columnExpressionsDefinition, DomainModel domainModel, Object classId) {
        return fromExpressions(domainModel.parseExpressionArray(columnExpressionsDefinition, classId).getExpressions());
    }

    static ExpressionColumn[] fromJsonArrayOrExpressionsDefinition(String jsonOrDef, DomainModel domainModel, Object classId) {
        return (jsonOrDef.startsWith("[")) ?
            fromJsonArray(jsonOrDef, domainModel, classId)
            : fromExpressionsDefinition(jsonOrDef, domainModel, classId);
    }
}
