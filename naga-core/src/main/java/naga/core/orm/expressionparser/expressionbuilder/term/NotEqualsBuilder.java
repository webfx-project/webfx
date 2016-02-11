package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.NotEquals;

/**
 * @author Bruno Salmon
 */
public class NotEqualsBuilder extends BooleanExpressionBuilder {

    public NotEqualsBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected NotEquals newBinaryOperation(Expression left, Expression right) {
        return new NotEquals(left, right);
    }
}
