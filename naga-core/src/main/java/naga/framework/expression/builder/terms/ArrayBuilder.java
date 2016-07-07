package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Array;

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
