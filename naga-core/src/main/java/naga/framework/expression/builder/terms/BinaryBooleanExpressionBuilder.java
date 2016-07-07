package naga.framework.expression.builder.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.terms.BinaryBooleanExpression;

/**
 * @author Bruno Salmon
 */
public abstract class BinaryBooleanExpressionBuilder extends BinaryExpressionBuilder {

    public BinaryBooleanExpressionBuilder(ExpressionBuilder left, ExpressionBuilder right) {
        super(left, right);
    }

    @Override
    public BinaryBooleanExpression build() {
        return (BinaryBooleanExpression) super.build();
    }

    protected abstract BinaryBooleanExpression newBinaryOperation(Expression left, Expression right);
}
