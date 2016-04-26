package naga.core.ngui.displayresultset;

import naga.core.orm.domainmodel.DomainModel;
import naga.core.orm.domainmodel.Label;
import naga.core.orm.expression.Expression;
import naga.core.spi.json.Json;
import naga.core.spi.json.JsonObject;

/**
 * @author Bruno Salmon
 */
public class DisplayColumn {

    private final Label label;
    private final String expressionDefinition;
    private Expression  expression;

    public DisplayColumn(String json) {
        this((JsonObject) Json.parse(json));
    }

    public DisplayColumn(JsonObject json) {
        this(json.get("label"), json.getString("expression"));
    }

    public DisplayColumn(Object label, String expressionDefinition) {
        this.expressionDefinition = expressionDefinition;
        this.expression = null;
        this.label = Label.from(label);
    }

    public DisplayColumn(Object label, Expression expression) {
        this.expressionDefinition = null;
        this.expression = expression;
        this.label = Label.from(label);
    }

    public Label getLabel() {
        return label;
    }

    public Expression getExpression() {
        return expression;
    }

    public void parseIfNecessary(DomainModel domainModel, Object classId) {
        if (expression == null)
            expression = domainModel.parseExpression(expressionDefinition, classId);
    }
}
