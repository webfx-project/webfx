package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.Minus;

/**
 * @author Bruno Salmon
 */
public class MinusBuilder extends BinaryExpressionBuilder {

    public MinusBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    protected Minus newBinaryOperation(Expression left, Expression right) {
        return new Minus(left, right);
    }
}
