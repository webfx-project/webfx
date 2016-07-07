package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.GreaterThanOrEquals;

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
