package naga.core.orm.mapping.display;

import naga.core.orm.domainmodel.Label;
import naga.core.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public class DisplayColumnMapping {

    private final Expression  expression;

    private final Label label;

    public DisplayColumnMapping(Expression expression, Label label) {
        this.expression = expression;
        this.label = label;
    }

    public Expression getExpression() {
        return expression;
    }

    public Label getLabel() {
        return label;
    }
}
