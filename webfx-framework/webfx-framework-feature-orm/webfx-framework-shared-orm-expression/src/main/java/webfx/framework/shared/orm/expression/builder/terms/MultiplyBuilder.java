package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.Multiply;

/**
 * @author Bruno Salmon
 */
public final class MultiplyBuilder extends BinaryExpressionBuilder {

    public MultiplyBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Multiply newBinaryOperation(Expression left, Expression right) {
        return new Multiply(left, right);
    }
}
