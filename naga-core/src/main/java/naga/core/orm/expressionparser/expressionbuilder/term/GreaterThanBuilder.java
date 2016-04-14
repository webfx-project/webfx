package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.GreaterThan;

/**
 * @author Bruno Salmon
 */
public class GreaterThanBuilder extends BinaryBooleanExpressionBuilder {

    public GreaterThanBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected GreaterThan newBinaryOperation(Expression left, Expression right) {
        return new GreaterThan(left, right);
    }
}
