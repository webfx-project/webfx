package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.GreaterThanOrEquals;

/**
 * @author Bruno Salmon
 */
public class GreaterThanOrEqualsBuilder extends BinaryBooleanExpressionBuilder {

    public GreaterThanOrEqualsBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected GreaterThanOrEquals newBinaryOperation(Expression left, Expression right) {
        return new GreaterThanOrEquals(left, right);
    }
}
