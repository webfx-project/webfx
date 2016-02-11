package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Array;

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
