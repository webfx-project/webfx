package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.Minus;

/**
 * @author Bruno Salmon
 */
public final class MinusBuilder extends BinaryExpressionBuilder {

    public MinusBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Minus newBinaryOperation(Expression left, Expression right) {
        return new Minus(left, right);
    }
}
