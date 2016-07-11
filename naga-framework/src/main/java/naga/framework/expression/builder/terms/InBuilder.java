package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.In;

/**
 * @author Bruno Salmon
 */
public class InBuilder extends BinaryBooleanExpressionBuilder {

    public InBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected In newBinaryOperation(Expression left, Expression right) {
        return new In(left, right);
    }
}
