package webfx.framework.expression.builder.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.terms.Array;

/**
 * @author Bruno Salmon
 */
public class ArrayBuilder extends UnaryExpressionBuilder {

    public ArrayBuilder(ExpressionBuilder operand) {
        super(operand);
    }

    @Override
    protected Array newUnaryOperation(Expression operand) {
        return new Array(operand);
    }
}
