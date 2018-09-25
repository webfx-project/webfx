package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.GreaterThanOrEquals;

/**
 * @author Bruno Salmon
 */
public final class GreaterThanOrEqualsBuilder extends BinaryBooleanExpressionBuilder {

    public GreaterThanOrEqualsBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected GreaterThanOrEquals newBinaryOperation(Expression left, Expression right) {
        return new GreaterThanOrEquals(left, right);
    }
}
