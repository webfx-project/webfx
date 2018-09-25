package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.GreaterThan;

/**
 * @author Bruno Salmon
 */
public final class GreaterThanBuilder extends BinaryBooleanExpressionBuilder {

    public GreaterThanBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected GreaterThan newBinaryOperation(Expression left, Expression right) {
        return new GreaterThan(left, right);
    }
}
