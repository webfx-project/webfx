package webfx.framework.shared.expression.builder.terms;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.terms.In;

/**
 * @author Bruno Salmon
 */
public final class InBuilder extends BinaryBooleanExpressionBuilder {

    public InBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected In newBinaryOperation(Expression left, Expression right) {
        return new In(left, right);
    }
}
