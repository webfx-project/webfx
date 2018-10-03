package webfx.framework.shared.expression.builder.terms;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.terms.Array;

/**
 * @author Bruno Salmon
 */
public final class ArrayBuilder extends UnaryExpressionBuilder {

    public ArrayBuilder(ExpressionBuilder operand) {
        super(operand);
    }

    @Override
    protected Array newUnaryOperation(Expression operand) {
        return new Array(operand);
    }
}
