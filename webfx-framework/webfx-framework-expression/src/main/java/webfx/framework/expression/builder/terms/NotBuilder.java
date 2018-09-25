package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.Not;

/**
 * @author Bruno Salmon
 */
public final class NotBuilder extends UnaryExpressionBuilder {

    public NotBuilder(ExpressionBuilder operand) {
        super(operand);
    }

    @Override
    protected Not newUnaryOperation(Expression operand) {
        return new Not(operand);
    }
}
