package webfx.framework.shared.orm.expression.builder.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.BinaryBooleanExpression;

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
