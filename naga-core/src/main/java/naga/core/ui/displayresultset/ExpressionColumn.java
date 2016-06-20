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

    public ExpressionColumn(String expressionDefinition, Object label, Converter expressionFormatter) {
        this.expressionDefinition = expressionDefinition;
        this.label = label;
        this.expressionFormatter = expressionFormatter;
    }

    public ExpressionColumn(Expression expression, Object label, Converter expressionFormatter) {
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
            return ExpressionColumn.create(Json.parseObject(jsonOrExpressionDefinition));
        return ExpressionColumn.create(jsonOrExpressionDefinition, null);
    }

    public static ExpressionColumn create(JsonObject json) {
        return ExpressionColumn.create(json.getString("expression"), json.get("label"));
    }

    public static ExpressionColumn create(String expressionDefinition, Object label) {
        return ExpressionColumn.create(expressionDefinition, label, null);
    }

    public static ExpressionColumn create(String expressionDefinition, Converter expressionFormatter) {
        return ExpressionColumn.create(expressionDefinition, null, expressionFormatter);
    }

    public static ExpressionColumn create(String expressionDefinition, Object label, Converter expressionFormatter) {
        return new ExpressionColumn(expressionDefinition, label, expressionFormatter);
    }

    public static ExpressionColumn create(Expression expression) {
        return new ExpressionColumn(expression, null, null);
    }
}
