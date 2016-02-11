package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.In;

/**
 * @author Bruno Salmon
 */
public class InBuilder extends BooleanExpressionBuilder {

    public InBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected In newBinaryOperation(Expression left, Expression right) {
        return new In(left, right);
    }
}
