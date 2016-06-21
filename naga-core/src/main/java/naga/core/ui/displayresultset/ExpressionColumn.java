package naga.core.ui.displayresultset;

import naga.core.json.Json;
import naga.core.json.JsonObject;
import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.domainmodel.Label;
import naga.core.orm.expression.Expression;
import naga.core.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public class ExpressionColumn {

    private final String expressionDefinition;
    private Expression  expression;
    private final Converter expressionFormatter;
    private Object label;
    private DisplayColumn displayColumn;

    private ExpressionColumn(String expressionDefinition, Object label, Converter expressionFormatter) {
        this.expressionDefinition = expressionDefinition;
        this.label = label;
        this.expressionFormatter = expressionFormatter;
    }

    private ExpressionColumn(Expression expression, Object label, Converter expressionFormatter) {
        this.expressionDefinition = null;
        this.expression = expression;
        this.label = label;
        this.expressionFormatter = expressionFormatter;
    }

    public DisplayColumn getDisplayColumn() {
        if (displayColumn == null) {
            if (label == null)
                label = Label.from(expression);
            displayColumn = new DisplayColumn(label, expression.getType());
        }
        return displayColumn;
    }

    public Expression getExpression() {
        return expression;
    }

    public Converter getExpressionFormatter() {
        return expressionFormatter;
    }

    public void parseIfNecessary(DomainModel domainModel, Object classId) {
        if (expression == null)
            expression = domainModel.parseExpression(expressionDefinition, classId);
    }

    public static ExpressionColumn create(String jsonOrExpressionDefinition) {
        if (jsonOrExpressionDefinition.startsWith("{"))
            return create(Json.parseObject(jsonOrExpressionDefinition));
        return create(jsonOrExpressionDefinition, null);
    }

    public static ExpressionColumn create(JsonObject json) {
        return create(json.getString("expression"), json.get("label"));
    }

    public static ExpressionColumn create(String expressionDefinition, Object label) {
        return create(expressionDefinition, label, null);
    }

    public static ExpressionColumn create(String expressionDefinition, Converter expressionFormatter) {
        return create(expressionDefinition, null, expressionFormatter);
    }

    public static ExpressionColumn create(String expressionDefinition, Object label, Converter expressionFormatter) {
        return new ExpressionColumn(expressionDefinition, label, expressionFormatter);
    }

    public static ExpressionColumn create(Expression expression) {
        return new ExpressionColumn(expression, null, null);
    }

    public static ExpressionColumn[] fromExpressions(Expression[] columnExpressions) {
        ExpressionColumn[] expressionColumns = new ExpressionColumn[columnExpressions.length];
        int columnIndex = 0;
        for (Expression columnExpression : columnExpressions)
            expressionColumns[columnIndex++] = ExpressionColumn.create(columnExpression);
        return expressionColumns;
    }

    public static ExpressionColumn[] fromExpressionsDefinition(String columnExpressionsDefinition, DomainModel domainModel, Object classId) {
        return fromExpressions(domainModel.parseExpressionArray(columnExpressionsDefinition, classId).getExpressions());
    }
}
