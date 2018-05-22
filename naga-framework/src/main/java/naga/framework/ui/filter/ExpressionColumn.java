package naga.framework.ui.filter;

import naga.framework.ui.formatter.Formatter;
import naga.framework.ui.formatter.FormatterRegistry;
import naga.platform.services.json.Json;
import naga.platform.services.json.JsonArray;
import naga.platform.services.json.JsonObject;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.expression.Expression;
import naga.fxdata.displaydata.DisplayColumn;

/**
 * @author Bruno Salmon
 */
public interface ExpressionColumn {

    /**
     * @return the expression to be used to evaluate all values of the display result set for that column.
     */
    Expression getExpression();

    /**
     * @return the formatter to apply after the expression has been evaluated.
     */
    Formatter getExpressionFormatter();

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
}
