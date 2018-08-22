package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Not;

/**
 * @author Bruno Salmon
 */
public class NotBuilder extends UnaryExpressionBuilder {

    public NotBuilder(ExpressionBuilder operand) {
        super(operand);
    }

    @Override
    protected Not newUnaryOperation(Expression operand) {
        return new Not(operand);
    }
}
