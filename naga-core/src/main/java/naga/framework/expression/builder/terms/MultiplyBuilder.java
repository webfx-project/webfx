package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Multiply;

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
