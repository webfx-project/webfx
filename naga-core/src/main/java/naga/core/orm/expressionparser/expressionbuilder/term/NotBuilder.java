package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Not;

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
