package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Plus;

/**
 * @author Bruno Salmon
 */
public class PlusBuilder extends BinaryExpressionBuilder {

    public PlusBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Plus newBinaryOperation(Expression left, Expression right) {
        return new Plus(left, right);
    }
}
