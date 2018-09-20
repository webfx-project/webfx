package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.Equals;

/**
 * @author Bruno Salmon
 */
public class EqualsBuilder extends BinaryBooleanExpressionBuilder {

    public EqualsBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Equals newBinaryOperation(Expression left, Expression right) {
        return new Equals(left, right);
    }
}
