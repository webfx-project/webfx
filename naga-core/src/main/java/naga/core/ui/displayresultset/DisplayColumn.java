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
public class DisplayColumn {

    private final Label label;
    private final String expressionDefinition;
    private Expression  expression;
    private final Converter formatter;

    public DisplayColumn(String json) {
        this(Json.parseObject(json));
    }

    public DisplayColumn(JsonObject json) {
        this(json.get("label"), json.getString("expression"));
    }

    public DisplayColumn(Object label, String expressionDefinition) {
        this(label, expressionDefinition, null);
    }

    public DisplayColumn(Object label, String expressionDefinition, Converter formatter) {
        this.expressionDefinition = expressionDefinition;
        this.expression = null;
        this.label = Label.from(label);
        this.formatter = formatter;
    }

    public DisplayColumn(Object label, Expression expression) {
        this(label, expression, null);
    }

    public DisplayColumn(Object label, Expression expression, Converter formatter) {
        this.expressionDefinition = null;
        this.expression = expression;
        this.label = Label.from(label);
        this.formatter = formatter;
    }

    public Label getLabel() {
        return label;
    }

    public Expression getExpression() {
        return expression;
    }

    public Converter getFormatter() {
        return formatter;
    }

    public void parseIfNecessary(DomainModel domainModel, Object classId) {
        if (expression == null)
            expression = domainModel.parseExpression(expressionDefinition, classId);
    }
}
