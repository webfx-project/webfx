package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.Not;

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
