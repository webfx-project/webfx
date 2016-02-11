package naga.core.orm.expressionparser.expressionbuilder.term;

import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.Multiply;

/**
 * @author Bruno Salmon
 */
public class MultiplyBuilder extends BinaryExpressionBuilder {

    public MultiplyBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Multiply newBinaryOperation(Expression left, Expression right) {
        return new Multiply(left, right);
    }
}
