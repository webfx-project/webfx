package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.Minus;

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
