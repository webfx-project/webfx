package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Or;

/**
 * @author Bruno Salmon
 */
public class OrBuilder extends BinaryExpressionBuilder {

    public OrBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Or newBinaryOperation(Expression left, Expression right) {
        return new Or(left, right);
    }
}
