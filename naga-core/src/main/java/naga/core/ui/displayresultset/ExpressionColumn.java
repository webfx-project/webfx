package naga.core.ui.displayresultset;

import naga.core.json.Json;
import naga.core.json.JsonObject;
import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.expression.Expression;
import naga.core.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public class ExpressionColumn {

    private final String expressionDefinition;
    private Expression  expression;
    private final Converter expressionFormatter;
    private final Object label;
    private DisplayColumn displayColumn;

    public ExpressionColumn(String json) {
        this(Json.parseObject(json));
    }

    public ExpressionColumn(JsonObject json) {
        this(json.get("label"), json.getString("expression"));
    }

    public ExpressionColumn(Object label, String expressionDefinition) {
        this(label, expressionDefinition, null);
    }

    public ExpressionColumn(Object label, String expressionDefinition, Converter expressionFormatter) {
        this.expressionDefinition = expressionDefinition;
        this.label = label;
        this.expressionFormatter = expressionFormatter;
    }

    public ExpressionColumn(Object label, Expression expression) {
        this(label, expression, null);
    }

    public ExpressionColumn(Object label, Expression expression, Converter expressionFormatter) {
        this.expressionDefinition = null;
        this.expression = expression;
        this.label = label;
        this.expressionFormatter = expressionFormatter;
    }

    public DisplayColumn getDisplayColumn() {
        if (displayColumn == null)
            displayColumn = new DisplayColumn(label, expression.getType());
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
}
